package com.felixrilling.clingy4j.lookup;

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
     * @param pathDangling Dangling pathUsed.
     * @param pathUsed     Path used.
     * @param missing      Key of missing command.
     * @param similar      Similar keys available.
     */
    LookupErrorNotFound(List<String> pathDangling, List<String> pathUsed, String missing, List<String> similar) {
        super(false, ResultType.ERROR_NOT_FOUND, pathDangling, pathUsed);
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
