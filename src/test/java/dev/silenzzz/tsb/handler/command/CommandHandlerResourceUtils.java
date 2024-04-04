package dev.silenzzz.tsb.handler.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class CommandHandlerResourceUtils {

    @SneakyThrows
    public static List<File> getCommandHandlersClasses() {
        File file = ResourceUtils.getFile("classpath:../classes/dev/silenzzz/tsb/handler/command/");
        return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .filter(f -> f.getName().endsWith(".class"))
                .toList();
    }
}
