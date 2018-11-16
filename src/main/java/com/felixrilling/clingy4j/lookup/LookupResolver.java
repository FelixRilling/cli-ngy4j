package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.argument.ArgumentMatcher;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Lookup tools for resolving paths through {@link CommandMap}s.
 */
public class LookupResolver {

    private static final Logger logger = LoggerFactory.getLogger(LookupResolver.class);

    private final boolean caseSensitive;

    /**
     * Creates a new {@link LookupResolver}.
     *
     * @param caseSensitive If the lookup should honor case.
     */
    public LookupResolver(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Resolves a path through a {@link CommandMap}.
     *
     * @param mapAliased     Map to use.
     * @param path           Path to getPathUsed.
     * @param parseArguments If dangling path items should be treated as arguments.
     * @return Lookup result, either {@link LookupSuccess}, {@link LookupErrorNotFound} or {@link LookupErrorMissingArgs}.
     */
    public LookupResult resolve(CommandMap mapAliased, List<String> path, boolean parseArguments) {
        if (path.isEmpty())
            throw new IllegalArgumentException("Path cannot be empty.");

        return resolveInternal(mapAliased, path, new LinkedList<>(), parseArguments);
    }

    private LookupResult resolveInternal(CommandMap mapAliased, List<String> path, List<String> pathUsed, boolean parseArguments) {
        String currentPathFragment = path.get(0);
        List<String> pathNew = path.subList(1, path.size());
        pathUsed.add(currentPathFragment);

        if (caseSensitive ? !mapAliased.containsKey(currentPathFragment) : !mapAliased.containsKeyIgnoreCase(currentPathFragment)) {
            logger.warn("Command '{}' could not be found.", currentPathFragment);
            return new LookupErrorNotFound(pathNew, pathUsed, currentPathFragment, CommandUtil.getSimilar(mapAliased, currentPathFragment));
        }

        Command command = caseSensitive ? mapAliased.get(currentPathFragment) : mapAliased.getIgnoreCase(currentPathFragment);
        logger.debug("Successfully looked up command: {}", currentPathFragment);

        if (!pathNew.isEmpty() && command.getSub() != null) {
            logger.trace("Resolving sub-commands: {} {}", command.getSub(), pathNew);
            return resolveInternal(command.getSub().getMapAliased(), pathNew, pathUsed, parseArguments);
        }

        Map<String, String> argumentsResolved;
        if (!parseArguments || command.getArgs() == null || command.getArgs().isEmpty()) {
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
}

