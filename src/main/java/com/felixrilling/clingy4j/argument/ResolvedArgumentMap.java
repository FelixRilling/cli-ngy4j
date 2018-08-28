package com.felixrilling.clingy4j.argument;

import java.util.HashMap;

/**
 * Map of resolved {@link Argument}s. Returned by {@link ArgumentMatcher#getResult}.
 */
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
