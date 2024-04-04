package dev.silenzzz.tsb.handler.command;

import dev.silenzzz.tsb.handler.command.cor.CommandHandler;
import dev.silenzzz.tsb.handler.command.util.CommandHandlersRegistry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 */
class CommandHandlersRegistryTest {

    @Test
    void shouldInitializeAndAddToRegistryAllCommandHandlers() {
        List<File> commandHandlersClasses = CommandHandlerResourceUtils.getCommandHandlersClasses();

        Collection<CommandHandler> handlers = CommandHandlersRegistry.getAllCommandHandlers();

        assertThat(handlers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(commandHandlersClasses.size())
                .hasOnlyElementsOfType(CommandHandler.class)
                .allSatisfy(h -> {
                    assertThat(handlers)
                            .as("Check if all handler class files was detected and instantiated")
                            .map(handler -> handler.getClass().getSimpleName() + ".class")
                            .containsAll(commandHandlersClasses.stream().map(File::getName).toList());
                });
    }
}
