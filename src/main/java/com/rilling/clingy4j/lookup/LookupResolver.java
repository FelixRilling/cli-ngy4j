package com.rilling.clingy4j.lookup;

import com.rilling.clingy4j.argument.ArgumentMatcher;
import com.rilling.clingy4j.command.Command;
import com.rilling.clingy4j.command.CommandMap;
import com.rilling.clingy4j.command.CommandUtil;
import com.rilling.clingy4j.lookup.result.LookupErrorMissingArgs;
import com.rilling.clingy4j.lookup.result.LookupErrorNotFound;
import com.rilling.clingy4j.lookup.result.LookupResult;
import com.rilling.clingy4j.lookup.result.LookupSuccess;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Lookup tools for resolving paths through {@link CommandMap}s.
 */
public class LookupResolver {

    private static final Logger logger = LoggerFactory.getLogger(LookupResolver.class);

    private final CaseSensitivity caseSensitivity;

    /**
     * Creates a new {@link LookupResolver}.
     *
     * @param caseSensitivity If the lookup should honor case.
     */
    public LookupResolver(@NotNull CaseSensitivity caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
    }

    /**
     * Resolves a path through a {@link CommandMap}.
     *
     * @param mapAliased        Map to use.
     * @param path              Path to getPathUsed.
     * @param argumentResolving If dangling path items should be treated as arguments.
     * @return Lookup result, either {@link LookupSuccess}, {@link LookupErrorNotFound} or {@link LookupErrorMissingArgs}.
     */
    @NotNull
    public LookupResult resolve(@NotNull CommandMap mapAliased, @NotNull List<String> path, @NotNull ArgumentResolving argumentResolving) {
        if (path.isEmpty())
            throw new IllegalArgumentException("Path cannot be empty.");

        return resolveInternal(mapAliased, path, new LinkedList<>(), argumentResolving);
    }

    @NotNull
    private LookupResult resolveInternal(@NotNull CommandMap mapAliased, @NotNull List<String> path, @NotNull List<String> pathUsed, @NotNull ArgumentResolving argumentResolving) {
        String currentPathFragment = path.get(0);
        List<String> pathNew = path.subList(1, path.size());
        pathUsed.add(currentPathFragment);

        if (!hasCommand(mapAliased, currentPathFragment)) {
            logger.warn("Command '{}' could not be found.", currentPathFragment);
            return new LookupErrorNotFound(pathNew, pathUsed, currentPathFragment, CommandUtil.getSimilar(mapAliased, currentPathFragment));
        }

        Command command = mapAliased.getCommand(currentPathFragment, caseSensitivity);
        Objects.requireNonNull(command); // We already checked if the key exists, but safe is safe.
        logger.debug("Found command: '{}'", currentPathFragment);

        /*
         * Recurse into sub-commands if:
         * Additional items are in the path AND
         * the current command has sub-commands AND
         * the sub-commands contain the next path item.
         */
        if (!pathNew.isEmpty() && command.getSub() != null && hasCommand(command.getSub().getMapAliased(), pathNew.get(0))) {
            return resolveInternalSub(pathNew, pathUsed, command, argumentResolving);
        }

        /*
         * Skip checking for arguments if:
         * The parameter argumentResolving is set to ignore arguments OR
         * the command has no arguments defined OR
         * the command has an empty array defined as arguments.
         */
        Map<String, String> argumentsResolved;
        if (argumentResolving == ArgumentResolving.IGNORE || command.getArgs() == null || command.getArgs().isEmpty()) {
            logger.debug("No arguments defined, using empty list.");
            argumentsResolved = new HashMap<>();
        } else {
            logger.debug("Looking up arguments: {}", pathNew);
            ArgumentMatcher argumentMatcher = new ArgumentMatcher(command.getArgs(), pathNew);

            if (!argumentMatcher.getMissing().isEmpty()) {
                logger.warn("Some arguments could not be found: {}", argumentMatcher.getMissing());
                return new LookupErrorMissingArgs(pathNew, pathUsed, argumentMatcher.getMissing());
            }

            argumentsResolved = argumentMatcher.getResult();
            logger.debug("Successfully looked up arguments: {}", argumentsResolved);
        }

        LookupSuccess lookupSuccess = new LookupSuccess(pathNew, pathUsed, command, argumentsResolved);
        logger.debug("Returning successful lookup result: {}", lookupSuccess);

        return lookupSuccess;
    }

    @NotNull
    private LookupResult resolveInternalSub(List<String> pathNew, @NotNull List<String> pathUsed, Command command, @NotNull ArgumentResolving argumentResolving) {
        logger.trace("Resolving sub-commands: {} {}", command.getSub(), pathNew);
        return resolveInternal(command.getSub().getMapAliased(), pathNew, pathUsed, argumentResolving);
    }

    private boolean hasCommand(@NotNull CommandMap mapAliased, @NotNull String currentPathFragment) {
        return mapAliased.containsCommandKey(currentPathFragment, caseSensitivity);
    }

}

