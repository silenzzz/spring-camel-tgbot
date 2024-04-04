package dev.silenzzz.tsb.handler.command.util;

import com.google.common.collect.ImmutableMap;
import dev.silenzzz.tsb.handler.command.cor.CommandHandler;
import dev.silenzzz.tsb.handler.command.exception.CommandHandlerNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * This class contains all command handlers classes in map (command string value as key, hander as value).
 */
@Component
@Scope(value = "singleton")
@Slf4j
@RequiredArgsConstructor
public class CommandHandlersRegistry {

    private Map<String, CommandHandler> registry;

    private final CommandHandlerClassScanner scanner;

    @PostConstruct
    private void init() {
        Collection<CommandHandler> handlers = scanner.getHandlers(); // NOSONAR
        registry = ImmutableMap.copyOf(handlers.stream()
                .collect(Collectors.toMap(CommandHandler::getCommandValue, h -> h)));
    }

    /**
     * Return handler class by command string value.
     *
     * @param command Command string value
     * @return command handler class
     * @throws CommandHandlerNotFoundException Thrown if handler class not found by string value in registry
     */
    public CommandHandler getCommandHandlerByName(String command) throws CommandHandlerNotFoundException {
        return Optional.ofNullable(registry.get(command)).orElseThrow(() ->
                new CommandHandlerNotFoundException(String.format("Handler with command %s not found", command)));
    }

    /**
     * Get all command handlers.
     *
     * @return Collection of command handler classes
     */
    public Collection<CommandHandler> getAllCommandHandlers() {
        return registry.values();
    }
}
