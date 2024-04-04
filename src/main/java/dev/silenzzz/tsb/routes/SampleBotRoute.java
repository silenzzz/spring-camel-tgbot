package dev.silenzzz.tsb.routes;

import dev.silenzzz.tsb.config.BotConfigurationProperties;
import dev.silenzzz.tsb.handler.command.UnknownCommandHandler;
import dev.silenzzz.tsb.handler.command.cor.CommandHandler;
import dev.silenzzz.tsb.handler.command.exception.CommandHandlerNotFoundException;
import dev.silenzzz.tsb.handler.command.util.CommandHandlersRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    private final CommandHandlersRegistry registry;

    private URIBuilder telegramUri;

    /**
     * Initialize camel route.
     */
    @PostConstruct
    public void init() {
        telegramUri = new URIBuilder()
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
        from(telegramUri.build().toString())
                .routeId("main-bot-route")
                .to("log:INFO?showHeaders=true")
                .log("New message received")
                .choice()
                    .when(exchange -> {
                        IncomingMessage message = (IncomingMessage) exchange.getIn().getBody();
                        if (Optional.ofNullable(message).isPresent()) {
                            return !StringUtils.isBlank(message.getText());
                        }
                        return false;
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
                .process(new CommandProcessor())
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

    private class CommandProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            CommandHandler handler;

            try {
                handler = registry.getCommandHandlerByName(((IncomingMessage) exchange.getIn().getBody()).getText()); // NOSONAR
            } catch (CommandHandlerNotFoundException e) {
                handler = new UnknownCommandHandler();
            }

            handler.handle(exchange);
        }
    }
}
