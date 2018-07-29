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
 *
 */
public class LookupResolver {

    private final boolean caseSensitive;

    private final Logger logger = LoggerFactory.getLogger(LookupResolver.class);

    public LookupResolver() {
        this(true);
    }

    public LookupResolver(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public LookupResult resolve(CommandMap mapAliased, List<String> path) {
        return resolve(mapAliased, path, false);
    }

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
        logger.trace("Successfully looked up command: {}", command);

        if (pathNew.size() > 1 && command.getSub() != null) {
            logger.trace("Resolving sub-commands: {} {}", command.getSub(), pathNew);
            return resolve(command.getSub().getAliasedMap(), pathNew, pathUsed, parseArguments);
        }

        ResolvedArgumentMap argumentsResolved = null;
        if (command.getArgs() != null && !command.getArgs().isEmpty()) {
            logger.trace("Looking up arguments: {}", pathNew);
            ArgumentMatcher argumentMatcher = new ArgumentMatcher(command.getArgs(), pathNew);

            List<Argument> argumentsMissing = argumentMatcher.getMissing();
            if (!argumentsMissing.isEmpty()) {
                logger.warn("Some arguments could not be found: {}", argumentsMissing);
                return new LookupErrorMissingArgs(path, pathUsed, argumentsMissing);
            }

            argumentsResolved = argumentMatcher.getResult();
            logger.trace("Successfully looked up arguments: {}", argumentsResolved);
        }


        LookupSuccess lookupSuccess = new LookupSuccess(pathUsed, pathNew, command, argumentsResolved);
        logger.trace("Returning successful lookup result: {}", lookupSuccess);

        return lookupSuccess;
    }
}

