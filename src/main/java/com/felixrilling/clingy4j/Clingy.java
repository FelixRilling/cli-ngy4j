package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.ArgumentResolving;
import com.felixrilling.clingy4j.lookup.CaseSensitivity;
import com.felixrilling.clingy4j.lookup.LookupResolver;
import com.felixrilling.clingy4j.lookup.result.LookupErrorMissingArgs;
import com.felixrilling.clingy4j.lookup.result.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.result.LookupResult;
import com.felixrilling.clingy4j.lookup.result.LookupSuccess;
import com.felixrilling.clingy4j.parser.InputParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core {@link Clingy} class, entry point for creation of a new instance.
 */
public class Clingy {

    private static final Logger logger = LoggerFactory.getLogger(Clingy.class);

    private final LookupResolver lookupResolver;
    private final InputParser inputParser;
    private final CommandMap map;
    private final CommandMap mapAliased;

    /**
     * @see Clingy#Clingy(Map, List, CaseSensitivity)
     */
    @SuppressWarnings({"unused"})
    public Clingy() {
        this(new HashMap<>());
    }

    /**
     * @see Clingy#Clingy(Map, List, CaseSensitivity)
     */
    @SuppressWarnings({"unused"})
    public Clingy(@NotNull Map<String, Command> commands) {
        this(commands, Collections.singletonList("\""), CaseSensitivity.SENSITIVE);
    }

    /**
     * Creates a new {@link Clingy} instance.
     *
     * @param commands        Map of commands to create the instance with.
     * @param legalQuotes     List of quotes to use when parsing strings.
     * @param caseSensitivity If commands names should be treated as case sensitive during lookup.
     */
    @SuppressWarnings({"unused"})
    public Clingy(@NotNull Map<String, Command> commands, @NotNull List<String> legalQuotes, @NotNull CaseSensitivity caseSensitivity) {
        lookupResolver = new LookupResolver(caseSensitivity);
        inputParser = new InputParser(legalQuotes);
        map = new CommandMap(commands);
        mapAliased = new CommandMap();
        updateAliases();
    }

    /**
     * Checks if a path resolves to a command.
     *
     * @param path Path to look up.
     * @return If the path resolves to a command.
     */
    @SuppressWarnings("unused")
    public boolean hasPath(@NotNull List<String> path) {
        LookupResult lookupResult = getPath(path);

        return lookupResult.isSuccessful();
    }

    /**
     * Resolves a path to a command.
     *
     * @param path Path to look up.
     * @return Lookup result, either {@link LookupSuccess} or {@link LookupErrorNotFound}.
     */
    @NotNull
    @SuppressWarnings({"unused", "WeakerAccess"})
    public LookupResult getPath(@NotNull List<String> path) {
        logger.debug("Resolving path: {}", path);
        return lookupResolver.resolve(mapAliased, path, ArgumentResolving.IGNORE);
    }

    /**
     * Parses a string into a command and arguments.
     *
     * @param input String to parse.
     * @return Lookup result, either {@link LookupSuccess}, {@link LookupErrorNotFound} or {@link LookupErrorMissingArgs}.
     */
    @NotNull
    @SuppressWarnings({"unused"})
    public LookupResult parse(@NotNull String input) {
        logger.debug("Parsing input: '{}'", input);
        return lookupResolver.resolve(mapAliased, inputParser.parse(input), ArgumentResolving.RESOLVE);
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public void setCommand(String key, Command command) {
        map.put(key, command);
        updateAliases();
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Command getCommand(String key) {
        return mapAliased.get(key);
    }

    @SuppressWarnings({"unused"})
    public boolean hasCommand(String key) {
        return mapAliased.containsKey(key);
    }

    @NotNull
    @SuppressWarnings({"unused"})
    public CommandMap getMap() {
        return map;
    }

    @NotNull
    @SuppressWarnings({"unused"})
    public CommandMap getMapAliased() {
        return mapAliased;
    }

    private void updateAliases() {
        logger.debug("Updating aliased map.");
        mapAliased.clear();
        mapAliased.putAll(map);

        for (Map.Entry<String, Command> entry : map.entrySet()) {
            for (String alias : entry.getValue().getAlias()) {
                if (mapAliased.containsKey(alias)) {
                    logger.warn("Alias '{}' conflicts with a previously defined key, will be ignored.", alias);
                } else {
                    logger.trace("Created alias '{}' for '{}'", alias, entry.getKey());
                    mapAliased.put(alias, entry.getValue());
                }
            }
        }

        logger.debug("Done updating aliased map.");
    }

}
