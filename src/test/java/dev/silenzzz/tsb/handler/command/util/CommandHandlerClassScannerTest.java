package dev.silenzzz.tsb.handler.command.util;

import dev.silenzzz.tsb.handler.command.CommandHandlerResourceUtils;
import dev.silenzzz.tsb.handler.command.UnknownCommandHandler;
import dev.silenzzz.tsb.handler.command.cor.CommandHandler;
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
class CommandHandlerClassScannerTest {

    @Test
    void shouldScanCommandHandlerClassesFromPackage() {
        Collection<CommandHandler> handlers = CommandHandlerClassScanner.getHandlers();

        List<File> handlersClassFiles = CommandHandlerResourceUtils.getCommandHandlersClasses();

        assertThat(handlers)
                .isNotNull()
                .isNotEmpty()
                .hasOnlyElementsOfType(CommandHandler.class)
                .hasSize(handlersClassFiles.size())
                .anySatisfy(c -> assertThat(handlers)
                        .as("At least one command handler must have UnknownCommandHandler as next handler in chain")
                        .matches((ignored) -> handlers.stream()
                                .anyMatch(h ->  h.getNext() instanceof UnknownCommandHandler),
                                "At least one command handler must have UnknownCommandHandler as next handler in chain")
                )
                .allSatisfy(c -> {
                    assertThat(handlers)
                            .as("All command values must be unique")
                            .allSatisfy((h) -> {
                                assertThat(handlers)
                                        .as("Check if all handler class files was detected and instantiated")
                                        .map(handler -> handler.getClass().getSimpleName() + ".class")
                                        .containsAll(handlersClassFiles.stream().map(File::getName).toList());

                                List<String> commandValues = handlers.stream()
                                        .map(CommandHandler::getCommandValue)
                                        .toList();

                                assertThat(commandValues)
                                        .isNotNull()
                                        .doesNotHaveDuplicates()
                                        .as("All command string values must starts with /")
                                        .allMatch(s -> s.startsWith("/"));
                            });

                    assertThat(c.getClass())
                            .as("Check if package is correct")
                            .hasPackage("dev.silenzzz.tsb.handler.command");

                    assertThat(c)
                            .as("Check if string command value is not null, except for UnknownCommandHandler")
                            .isNotNull()
                            .matches(h -> {
                                if (h instanceof UnknownCommandHandler) {
                                    return true;
                                }
                                return h.getCommandValue() != null;
                            });
                });
    }
}
