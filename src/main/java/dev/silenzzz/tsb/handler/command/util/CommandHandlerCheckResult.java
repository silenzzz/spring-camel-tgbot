package dev.silenzzz.tsb.handler.command.util;

import lombok.Value;

import javax.annotation.Nullable;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * Class that contins result of the validation by canHandle method.
 */
@Value
public class CommandHandlerCheckResult {

    boolean canHandle;

    @Nullable
    String cause;

}
