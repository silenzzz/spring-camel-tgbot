package dev.silenzzz.tsb.handler.command.util;

import com.google.common.collect.ImmutableList;
import dev.silenzzz.tsb.handler.command.UnknownCommandHandler;
import dev.silenzzz.tsb.handler.command.cor.BaseCommandHandler;
import dev.silenzzz.tsb.handler.command.cor.CommandHandler;
import dev.silenzzz.tsb.handler.command.exception.CommandHandlerInstantiationException;
import dev.silenzzz.tsb.handler.command.util.exception.CommandHandlerClassScannerException;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author silenzzz
 * @see <a href="http://www.silenzzz.dev">silenzzz.dev</a>
 * @see <a href="https://github.com/silenzzz">github.com/silenzzz</a>
 * @see <a href="mailto:silenzzzdev@gmail.com">silenzzz</a>
 * <p>
 * This class used for discovering command handlers classes in package (in this sample named "dev.silenzzz.tsb.handler.command")
 */
@Component
@Scope("singleton")
@Slf4j
public class CommandHandlerClassScanner {

    private static final String PACKAGE_NAME = "dev.silenzzz.tsb.handler.command";

    private Collection<CommandHandler> handlers;

    public CommandHandlerClassScanner() throws CommandHandlerClassScannerException {
        this.handlers = scan();
    }

    /**
     * Get all command handler classes.
     *
     * @return Collection of command handler classes
     */
    public Collection<CommandHandler> getHandlers() {
        return ImmutableList.copyOf(handlers);
    }

    private Collection<CommandHandler> scan() throws CommandHandlerClassScannerException {
        log.info("Start scanning for command handlers in package: {}", PACKAGE_NAME);
        try (InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(
                        String.format("../classes/%s/", PACKAGE_NAME.replace('.', '/')))) {

            @Cleanup
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)));
            handlers = reader.lines()
                    .filter(s -> s.endsWith(".class"))
                    .map(s -> {
                        try {
                            return mapClass(s);
                        } catch (ClassNotFoundException e) {
                            throw new ExceptionInInitializerError(e);
                        }
                    })
                    .map(c -> {
                        try {
                            return buildHandler(c);
                        } catch (CommandHandlerInstantiationException e) {
                            throw new ExceptionInInitializerError(e);
                        }
                    })
                    .toList();

            List<String> names = new ArrayList<>(handlers.stream().map(CommandHandler::getCommandValue).toList());
            names.remove("/"); // Remove UnknownCommandHandler

            ImmutableList.copyOf(handlers).stream()
                    .filter(h -> !(h instanceof UnknownCommandHandler))
                    .forEach(h -> {
                        if (names.size() == 1) { // If only one handler name left in names, where is no next handler for it
                            CommandHandler unknownCommandHandler = findByCommandValue("/");
                            h.setNext(unknownCommandHandler); // So we set UnknownCommandHandler as next handler in chain
                            log.info("UnknownCommandHandler was set to next in chain for handler: {}", h.getClass().getSimpleName());
                            return;
                        }

                        String handlerName = names.get(0);
                        String nextHandlerName = names.get(1);

                        log.info("Setting next handler for {} with {}", handlerName, nextHandlerName);

                        CommandHandler handler = findByCommandValue(handlerName);
                        CommandHandler nextHandler = findByCommandValue(nextHandlerName);

                        handler.setNext(nextHandler);

                        names.remove(nextHandlerName);
                    });
            return handlers;
        } catch (IOException e) {
            log.error("Command handlers classes scanner exception. Cause: {}, message: {}", e.getCause(), e.getMessage());
            throw new CommandHandlerClassScannerException(e);
        } finally {
            log.info("Command handlers scan ended. Found {} classes: {}. In package: {}", handlers.size(), handlers.toArray(), PACKAGE_NAME);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private CommandHandler findByCommandValue(@Nullable String commandValue) {
        return handlers.stream() // NOSONAR
                .filter(h -> h.getCommandValue().equals(commandValue))
                .findFirst()
                .get();
    }

    @SuppressWarnings("unchecked")
    private Class<? extends BaseCommandHandler> mapClass(String className) throws ClassNotFoundException {
        try {
            log.info("Mapping command handler class with name: {}", className);
            return (Class<? extends BaseCommandHandler>) Class.forName(
                    String.format("%s.%s", PACKAGE_NAME, className.replace(".class", "")));
        } catch (ClassNotFoundException e) {
            log.error("Class with given name = {} not found", className);
            throw e;
        }
    }

    private CommandHandler buildHandler(Class<? extends CommandHandler> clazz) throws CommandHandlerInstantiationException {
        try {
            log.info("Building command handler with name: {}", clazz.getSimpleName());
            return (CommandHandler) clazz.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Can't instantiate command with name: {}", clazz.getSimpleName());
            throw new CommandHandlerInstantiationException(e);
        }
    }
}
