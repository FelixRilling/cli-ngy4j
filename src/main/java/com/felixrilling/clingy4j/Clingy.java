package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.ICommand;
import com.felixrilling.clingy4j.lookup.LookupResolver;
import com.felixrilling.clingy4j.lookup.result.LookupErrorMissingArgs;
import com.felixrilling.clingy4j.lookup.result.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.result.LookupResult;
import com.felixrilling.clingy4j.lookup.result.LookupSuccess;
import com.felixrilling.clingy4j.parser.InputParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Core {@link Clingy} class, entry point for creation of a new instance.
 */
public class Clingy {

    private final Logger logger = LoggerFactory.getLogger(Clingy.class);
    private final LookupResolver lookupResolver;
    private final InputParser inputParser;
    private CommandMap map;
    private CommandMap aliasedMap;

    /**
     * @see Clingy#Clingy(CommandMap, boolean, List)
     */
    public Clingy() {
        this(new CommandMap());
    }

    /**
     * @see Clingy#Clingy(CommandMap, boolean, List)
     */
    public Clingy(CommandMap commands) {
        this(commands, true);
    }

    /**
     * @see Clingy#Clingy(CommandMap, boolean, List)
     */
    public Clingy(CommandMap commands, boolean caseSensitive) {
        this(commands, caseSensitive, Collections.singletonList("\""));
    }

    /**
     * Creates a new {@link Clingy} instance.
     *
     * @param commands      Map of commands to create the instance with.
     * @param caseSensitive If commands names should be treated as case sensitive during lookup.
     * @param legalQuotes   List of quotes to use when parsing strings.
     */
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

    /**
     * Resolves a path to a command.
     *
     * @param path Path to look up.
     * @return Lookup result, either {@link LookupSuccess} or {@link LookupErrorNotFound}.
     */
    public LookupResult resolve(List<String> path) {
        logger.debug("Resolving path: {}", path);
        return lookupResolver.resolve(aliasedMap, path);
    }

    /**
     * Parses a string into a command and arguments.
     *
     * @param input String to parse.
     * @return Lookup result, either {@link LookupSuccess}, {@link LookupErrorNotFound} or {@link LookupErrorMissingArgs}.
     */
    public LookupResult parse(String input) {
        logger.debug("Parsing input: '{}'", input);
        return lookupResolver.resolve(aliasedMap, inputParser.parse(input), true);
    }

    private void updateAliasedMap() {
        CommandMap result = new CommandMap(map);

        logger.debug("Updating aliased map.");

        for (Map.Entry<String, ICommand> entry : map.entrySet()) {
            for (String alias : entry.getValue().getAlias()) {
                if (result.containsKey(alias)) {
                    logger.warn("Alias '{}' conflicts with a previously defined key, will be ignored.", alias);
                } else {
                    logger.trace("Created alias '{}' for '{}'", alias, entry.getKey());
                    result.put(alias, entry.getValue());
                }
            }
        }

        logger.debug("Done updating aliased map.");
        aliasedMap = result;
    }

}
