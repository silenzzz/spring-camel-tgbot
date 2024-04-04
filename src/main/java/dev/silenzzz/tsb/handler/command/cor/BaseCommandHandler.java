package dev.silenzzz.tsb.handler.command.cor;

import dev.silenzzz.tsb.handler.command.UnknownCommandHandler;
import dev.silenzzz.tsb.handler.command.exception.CommandHandlingException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.Exchange;

import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Base "processor" in chain of responcibility pattern. Used to get rid of duplicate code in concreate handlers.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public abstract class BaseCommandHandler implements CommandHandler {

    protected CommandHandler next;

    /**
     * This method is used to validate if the current handler can handle the exchange or not.
     * If not, exchange will be sent to the next handler in a chain.
     *
     * @param exchange exchange from camel route
     * @param function function that will be executed in the handle method
     * @return exchange from camel route
     * @throws CommandHandlingException occurs if something went wrong during exchange handling in command handler
     */
    protected Exchange execute(Exchange exchange, UnaryOperator<Exchange> function) throws CommandHandlingException {
        return canHandle(exchange).isCanHandle() ?
                function.apply(exchange) :
                Optional.ofNullable(next.handle(exchange))
                        .orElse(new UnknownCommandHandler().handle(exchange));
    }
}
