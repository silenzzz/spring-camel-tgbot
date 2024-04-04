package dev.silenzzz.tsb.routes;

import dev.silenzzz.tsb.handler.command.exception.CommandHandlingException;
import dev.silenzzz.tsb.handler.command.util.CommandHandlersRegistry;
import dev.silenzzz.tsb.config.BotConfigurationProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Class describing camel route.
 */
@Component
@RequiredArgsConstructor
public class SampleBotRoute extends RouteBuilder {

    private final BotConfigurationProperties properties;

    private URIBuilder telegramUri;

    /**
     * Initialize camel route.
     */
    @PostConstruct
    public void init() {
        telegramUri = new URIBuilder() // telegr:123
                .setScheme("telegram")
                .setHost("bots")
                .addParameter("authorizationToken", properties.getApiToken())
                .addParameter("proxyType", "HTTP")
                .addParameter("sendEmptyMessageWhenIdle", "true");
    }

    /**
     * Configure camel route.
     *
     * @throws Exception Exceptionx
     */
    @Override
    public void configure() throws Exception {
        // @formatter:off IntelliJ IDEA will reformat custom line breaks in code block with route, if formatter is enabled
        from("telegram:bots")
                .routeId("main-bot-route")
                .to("log:INFO?showHeaders=true")
                .log("New message received")
                .choice()
                    .when(exchange -> {
                        IncomingMessage message = (IncomingMessage) exchange.getIn().getBody();
                        String text = message.getText();
                        return !StringUtils.isBlank(text);
                    })
                        .to("direct:messageHandler") // NOSONAR
                    .otherwise()
                        .to("direct:unsupportedOperationHandler")
                .end();

        from("direct:unsupportedOperationHandler")
                .routeId("Unsupported command without handler route")
                .log("Unsupported command received")
                .setBody(constant("Unsupported operation"))
                .to("direct:messageHandler");

        from("direct:messageHandler")
                .routeId("Message handler route")
                //.setBody(body())
                .log("Processing command")
                .doTry()
                    .bean(CommandHandlersRegistry.getCommandHandlerByName(body().convertToString().toString()), "handle")
                    .log("Command handler found")
                .doCatch(CommandHandlingException.class)
                    .to("direct:handlingError")
                .endDoCatch()
                .endDoTry()
                .end()
                .to("direct:sendMessage");

        from("direct:sendMessage")
                .routeId("Message sender route")
                .to(sendMessageUri());

        from("direct:handlingError")
                .routeId("Handling error sender route")
                .to(sendMessageUri());
        // @formatter:on Enabling IntelliJ IDEA formatter
    }

    private String sendMessageUri() {
        return telegramUri.addParameter("chatId", header("TELEGRAM_CHAT_ID").toString()).toString();
    }
}
