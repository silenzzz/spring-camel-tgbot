package dev.silenzzz.tsb.handler.command.exception;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Exception that occures if something went wrong during command handler instantiation.
 */
public class CommandHandlerInstantiationException extends ReflectiveOperationException {

    public CommandHandlerInstantiationException(Throwable cause) {
        super(cause);
    }
}
