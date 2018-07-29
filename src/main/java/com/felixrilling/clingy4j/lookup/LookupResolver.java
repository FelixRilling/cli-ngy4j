package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.argument.ArgumentMatcher;
import com.felixrilling.clingy4j.argument.ResolvedArgumentMap;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.ICommand;
import com.felixrilling.clingy4j.command.util.CommandUtil;
import com.felixrilling.clingy4j.lookup.result.LookupErrorMissingArgs;
import com.felixrilling.clingy4j.lookup.result.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.result.LookupResult;
import com.felixrilling.clingy4j.lookup.result.LookupSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Lookup tools for resolving paths through {@link CommandMap}s.
 */
public class LookupResolver {

    private final boolean caseSensitive;
    private final Logger logger = LoggerFactory.getLogger(LookupResolver.class);

    /**
     * @see LookupResolver#LookupResolver(boolean)
     */
    public LookupResolver() {
        this(true);
    }

    /**
     * Creates a new {@link LookupResolver}.
     *
     * @param caseSensitive If the lookup should honor case.
     */
    public LookupResolver(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * @see LookupResolver#resolve(CommandMap, List, List, boolean)
     */
    public LookupResult resolve(CommandMap mapAliased, List<String> path) {
        return resolve(mapAliased, path, false);
    }

    /**
     * Resolves a path through a {@link CommandMap}.
     *
     * @param mapAliased     Map to use.
     * @param path           Path to resolve.
     * @param parseArguments If dangling path items should be treated as arguments.
     * @return Lookup result, either {@link LookupSuccess}, {@link LookupErrorNotFound} or {@link LookupErrorMissingArgs}.
     */
    public LookupResult resolve(CommandMap mapAliased, List<String> path, boolean parseArguments) {
        return resolve(mapAliased, path, new LinkedList<>(), parseArguments);
    }

    private LookupResult resolve(CommandMap mapAliased, List<String> path, List<String> pathUsed, boolean parseArguments) {
        if (path.isEmpty()) {
            logger.info("Empty path was given, returning early.");
            return null;
        }

        String currentPathFragment = path.get(0);

        if (caseSensitive ? !mapAliased.containsKey(currentPathFragment) : !mapAliased.containsKeyIgnoreCase(currentPathFragment)) {
            logger.warn("Command '{}' could not be found.", currentPathFragment);
            return new LookupErrorNotFound(path, pathUsed, currentPathFragment, CommandUtil.getSimilar(mapAliased, currentPathFragment));
        }

        ICommand command = caseSensitive ? mapAliased.get(currentPathFragment) : mapAliased.getIgnoreCase(currentPathFragment);
        List<String> pathNew = path.subList(1, path.size());
        pathUsed.add(0, currentPathFragment);
        logger.debug("Successfully looked up command: {}", currentPathFragment);

        if (pathNew.size() > 1 && command.getSub() != null) {
            logger.trace("Resolving sub-commands: {} {}", command.getSub(), pathNew);
            return resolve(command.getSub().getAliasedMap(), pathNew, pathUsed, parseArguments);
        }

        ResolvedArgumentMap argumentsResolved;
        if (command.getArgs() == null || command.getArgs().isEmpty()) {
            logger.debug("No arguments defined, using empty list.");
            argumentsResolved = new ResolvedArgumentMap();
        } else {
            logger.debug("Looking up arguments: {}", pathNew);
            ArgumentMatcher argumentMatcher = new ArgumentMatcher(command.getArgs(), pathNew);

            List<Argument> argumentsMissing = argumentMatcher.getMissing();
            if (!argumentsMissing.isEmpty()) {
                logger.warn("Some arguments could not be found: {}", argumentsMissing);
                return new LookupErrorMissingArgs(path, pathUsed, argumentsMissing);
            }

            argumentsResolved = argumentMatcher.getResult();
            logger.debug("Successfully looked up arguments: {}", argumentsResolved);
        }


        LookupSuccess lookupSuccess = new LookupSuccess(pathUsed, pathNew, command, argumentsResolved);
        logger.debug("Returning successful lookup result: {}", lookupSuccess);

        return lookupSuccess;
    }
}

