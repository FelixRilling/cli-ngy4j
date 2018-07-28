package com.felixrilling.clingy4j.argument;

import java.util.HashMap;

public class ResolvedArgumentMap extends HashMap<String, String> {

    /**
     * Creates an empty {@link ResolvedArgumentMap}.
     */
    public ResolvedArgumentMap() {
        super();
    }

    /**
     * Creates an empty {@link ResolvedArgumentMap} with an initial capacity.
     */
    public ResolvedArgumentMap(int initialCapacity) {
        super(initialCapacity);
    }
}
