package dev.silenzzz.tsb.handler.command.util.exception;

import java.io.IOException;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Exception that throwed if something happend during handlers classes scanning.
 */
public class CommandHandlerClassScannerException extends IOException {

    public CommandHandlerClassScannerException(Throwable cause) {
        super(cause);
    }
}
