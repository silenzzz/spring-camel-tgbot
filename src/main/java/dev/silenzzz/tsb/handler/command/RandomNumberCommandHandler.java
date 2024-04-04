package dev.silenzzz.tsb.handler.command;

import dev.silenzzz.tsb.handler.command.cor.BaseCommandHandler;
import dev.silenzzz.tsb.handler.command.exception.CommandHandlingException;
import dev.silenzzz.tsb.handler.command.util.CommandHandlerCheckResult;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.camel.Exchange;

import java.util.Random;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Random number command handler.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class RandomNumberCommandHandler extends BaseCommandHandler {

    private static final Random random = new Random();

    @Override
    public String getCommandValue() {
        return "/random";
    }

    @Override
    public CommandHandlerCheckResult canHandle(Exchange exchange) {
        return new CommandHandlerCheckResult(true, null);
    }

    @Override
    public Exchange handle(Exchange exchange) throws CommandHandlingException {
        return execute(exchange, e -> {
            e.getIn().setBody(random.nextInt(0, 100));
            return e;
        });
    }
}
