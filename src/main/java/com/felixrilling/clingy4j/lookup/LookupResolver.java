package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.CommandUtil;
import com.felixrilling.clingy4j.command.ICommand;
import com.felixrilling.clingy4j.command.argument.CommandArgument;
import com.felixrilling.clingy4j.command.argument.CommandArgumentMatcher;
import com.felixrilling.clingy4j.command.argument.ResolvedArgumentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

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

    public ILookupResult resolve(CommandMap mapAliased, List<String> path) {
        return resolve(mapAliased, path, false);
    }

    public ILookupResult resolve(CommandMap mapAliased, List<String> path, boolean parseArguments) {
        return resolve(mapAliased, path, new LinkedList<>(), parseArguments);
    }

    private ILookupResult resolve(CommandMap mapAliased, List<String> path, List<String> pathUsed, boolean parseArguments) {
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

        ResolvedArgumentMap argumentsResolved = new ResolvedArgumentMap();
        if (command.getArgs() != null && !command.getArgs().isEmpty()) {
            logger.trace("Looking up arguments: {}", pathNew);
            CommandArgumentMatcher argumentMatcher = new CommandArgumentMatcher(command.getArgs(), pathNew);

            List<CommandArgument> argumentsMissing = argumentMatcher.getMissing();
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

