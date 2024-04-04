package dev.silenzzz.tsb.handler.command.exception;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Exception that occures if command handler was not found if package.
 */
public class CommandHandlerNotFoundException extends ClassNotFoundException {

    public CommandHandlerNotFoundException(String s) {
        super(s);
    }
}
