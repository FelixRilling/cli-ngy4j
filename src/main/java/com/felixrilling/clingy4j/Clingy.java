package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.ICommand;
import com.felixrilling.clingy4j.lookup.ILookupResult;
import com.felixrilling.clingy4j.lookup.LookupResolver;
import com.felixrilling.clingy4j.parser.InputParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Clingy {

    private CommandMap map;
    private CommandMap aliasedMap;

    private Logger logger = LoggerFactory.getLogger(Clingy.class);
    private LookupResolver lookupResolver;
    private InputParser inputParser;


    public Clingy() {
        this(new CommandMap());
    }


    public Clingy(CommandMap commands) {
        this(commands, false);
    }

    public Clingy(CommandMap commands, boolean caseSensitive) {
        this(commands, caseSensitive, Collections.singletonList("\""));
    }

    public Clingy(CommandMap commands, boolean caseSensitive, List<String> legalQuotes) {
        map = commands;
        lookupResolver = new LookupResolver(caseSensitive);
        inputParser = new InputParser(legalQuotes);
        updateAliasedMap();
    }

    public CommandMap getMap() {
        return map;
    }

    public CommandMap getAliasedMap() {
        return aliasedMap;
    }

    public void setCommand(String key, ICommand command) {
        map.put(key, command);
        updateAliasedMap();
    }

    public ICommand getCommand(String key) {
        return aliasedMap.get(key);
    }

    public boolean hasCommand(String key) {
        return aliasedMap.containsKey(key);
    }

    public ILookupResult resolve(List<String> path) {
        return lookupResolver.resolve(aliasedMap, path);
    }

    public ILookupResult parse(String input) {
        return inputParser.parse(aliasedMap, input);
    }

    private void updateAliasedMap() {
        CommandMap result = new CommandMap(map);

        logger.trace("Updating aliased map...");

        for (Map.Entry<String, ICommand> entry : map.entrySet()) {
            for (String alias : entry.getValue().getAlias()) {
                if (result.containsKey(alias)) {
                    logger.warn("Alias '{}' conflicts with a previously defined key, will be ignored.", alias);
                } else {
                    result.put(alias, entry.getValue());
                }
            }
        }

        logger.trace("Done updating aliased map.");

        aliasedMap = result;
    }
}
