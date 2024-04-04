package dev.silenzzz.tsb.handler.command.cor;

import dev.silenzzz.tsb.handler.command.exception.CommandHandlingException;
import dev.silenzzz.tsb.handler.command.util.CommandHandlerCheckResult;
import org.apache.camel.Exchange;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Interface for command handlers that processes text commands in camel route (ex. "/ping" "/random").
 * This is "processor" interface for a chain of responsibility pattern.
 */
public interface CommandHandler {

    /**
     * @return the command value as string. Ex. "/ping", "/random". Used in route to find the associated handler for this command
     */
    String getCommandValue();

    /**
     * Method that contains logic for certain command.
     *
     * @param exchange exchange from camel route
     * @return exchange from camel route
     * @throws CommandHandlingException if something wrong happened during handling
     */
    Exchange handle(Exchange exchange) throws CommandHandlingException;

    /**
     * Set the next handler in a chain.
     *
     * @param handler next handler in a chain
     */
    void setNext(CommandHandler handler);

    /**
     * Get the next handler in a chain.
     *
     * @return next handler in a chain
     */
    CommandHandler getNext();

    /**
     * Method for validation. Checks if a handler can perform handling of exchange or not. If not, it will be sent to the next handler in the chain.
     *
     * @param exchange exchange from camel route
     * @return object with results of the performed check if the current handler can perform exchange handling. Contains two fields: boolean canHandle and if not field with the cause of it, can be null
     */
    CommandHandlerCheckResult canHandle(Exchange exchange);
}
