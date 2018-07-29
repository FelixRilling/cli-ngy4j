package com.felixrilling.clingy4j.command;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Map containing {@link ICommand}s.
 */
public class CommandMap extends HashMap<String, ICommand> {

    /**
     * Creates an empty  {@link CommandMap}.
     */
    public CommandMap() {
        super();
    }

    /**
     * Creates an empty {@link CommandMap} with an initial capacity.
     */
    public CommandMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Creates a {@link CommandMap} from another  {@link CommandMap}.
     *
     * @param map The {@link CommandMap} to copy.
     */
    public CommandMap(CommandMap map) {
        super(map);
    }

    /**
     * Checks if the map contains a key, ignoring case.
     *
     * @param key Key to check for.
     * @return If the map contains a key, ignoring case.
     */
    public boolean containsKeyIgnoreCase(String key) {
        return keySet().stream().map(String::toLowerCase).collect(Collectors.toSet()).contains(key.toLowerCase());
    }

    /**
     * Returns the value for the key, ignoring case.
     *
     * @param key Key to check for.
     * @return The value for the key, ignoring case.
     */
    public ICommand getIgnoreCase(String key) {
        for (Entry<String, ICommand> entry : entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey()))
                return entry.getValue();
        }

        return null;
    }
}
