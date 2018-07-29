package com.felixrilling.clingy4j.lookup.result;

import java.util.List;

/**
 * {@link LookupResult} indicating a command was expected but not found.
 */
public class LookupErrorNotFound extends LookupResult {

    private final String missing;
    private final List<String> similar;

    /**
     * Creates a {@link LookupErrorNotFound}.
     *
     * @param path         Path used.
     * @param pathDangling Dangling path.
     * @param missing      Key of missing command.
     * @param similar      Similar keys available.
     */
    public LookupErrorNotFound(List<String> path, List<String> pathDangling, String missing, List<String> similar) {
        super(false, ResultType.ERROR_COMMAND_NOT_FOUND, path, pathDangling);
        this.missing = missing;
        this.similar = similar;
    }

    public String getMissing() {
        return missing;
    }

    public List<String> getSimilar() {
        return similar;
    }
}
