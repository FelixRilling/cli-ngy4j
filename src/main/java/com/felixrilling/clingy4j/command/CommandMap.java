package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.lookup.CaseSensitivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Map containing {@link Command}s.
 */
public class CommandMap extends HashMap<String, Command> {

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
     * Creates a {@link CommandMap} from another {@link Map}.
     *
     * @param map The {@link Map} to copy.
     */
    public CommandMap(@NotNull Map<String, Command> map) {
        super(map);
    }

    /**
     * Checks if the map contains a key, ignoring case.
     *
     * @param key             Key to check for.
     * @param caseSensitivity Case sensitivity to use.
     * @return If the map contains a key, ignoring case.
     */
    public boolean containsCommandKey(@NotNull String key, CaseSensitivity caseSensitivity) {
        if (caseSensitivity == CaseSensitivity.INSENSITIVE) {
            return keySet().stream().map(String::toLowerCase).collect(Collectors.toSet()).contains(key.toLowerCase());
        }

        return this.containsKey(key);
    }

    /**
     * Returns the value for the key, ignoring case.
     *
     * @param key             Key to check for.
     * @param caseSensitivity Case sensitivity to use.
     * @return The value for the key, ignoring case.
     */
    @Nullable
    public Command getCommand(@NotNull String key, CaseSensitivity caseSensitivity) {
        if (caseSensitivity == CaseSensitivity.INSENSITIVE) {
            for (Entry<String, Command> entry : entrySet()) {
                if (key.equalsIgnoreCase(entry.getKey()))
                    return entry.getValue();
            }

            return null;
        }

        return this.get(key);
    }
}
