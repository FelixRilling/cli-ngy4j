package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.CommandUtil;
import com.felixrilling.clingy4j.command.ICommand;
import com.felixrilling.clingy4j.command.argument.CommandArgumentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LookupResolver {

    private final boolean caseSensitive;

    private final Logger logger = LoggerFactory.getLogger(LookupResolver.class);

    public LookupResolver(boolean caseSensitive) {
        //TODO make this matter.
        this.caseSensitive = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public ILookupResult resolve(CommandMap mapAliased, List<String> path) {
        return resolve(mapAliased, path, false);
    }

    public ILookupResult resolve(CommandMap mapAliased, List<String> path, boolean parseArguments) {
        return resolve(mapAliased, path, new ArrayList<>(), parseArguments);
    }

    private ILookupResult resolve(CommandMap mapAliased, List<String> path, List<String> pathUsed, boolean parseArguments) {
        if (path.isEmpty()) {
            logger.info("Empty path was given, returning early.");
            return null;
        }

        String currentPathFragment = path.get(0);

        if (!mapAliased.containsKey(currentPathFragment)) {
            logger.warn("Command '{}' could not be found.", currentPathFragment);
            return new LookupErrorNotFound(path, pathUsed, currentPathFragment, CommandUtil.getSimilar(mapAliased, currentPathFragment));
        }

        ICommand command = mapAliased.get(currentPathFragment);
        List<String> pathNew = path.subList(1, path.size());
        pathUsed.add(0, currentPathFragment);

        if (pathNew.size() > 1 && command.getSub() != null) {
            logger.trace("Resolving sub-commands.");
            return resolve(command.getSub().getAliasedMap(), pathNew, pathUsed, parseArguments);
        }

        logger.trace("Returning successful lookup result.");
        return new LookupSuccess(pathUsed, pathNew, command, new CommandArgumentMap());
    }
}
