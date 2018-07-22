package com.felixrilling.clingy4j.command;

import java.util.HashMap;

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
}
