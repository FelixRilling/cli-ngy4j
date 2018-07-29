package com.felixrilling.clingy4j.lookup.result;

import com.felixrilling.clingy4j.lookup.LookupResolver;

import java.util.List;

/**
 * Abstract response of {@link LookupResolver}.
 */
public abstract class LookupResult {

    private final boolean successful;
    private final List<String> path;
    private final List<String> pathDangling;
    private final ResultType type;

    /**
     * Creates a new {@link LookupResult}.
     *
     * @param successful   If the lookup was successful.
     * @param type         Type of the result, see {@link ResultType}.
     * @param path         Path used.
     * @param pathDangling Dangling path.
     */
    public LookupResult(boolean successful, ResultType type, List<String> path, List<String> pathDangling) {
        this.successful = successful;
        this.type = type;
        this.path = path;
        this.pathDangling = pathDangling;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public ResultType getType() {
        return type;
    }

    public List<String> getPath() {
        return path;
    }

    public List<String> getPathDangling() {
        return pathDangling;
    }

    public enum ResultType {SUCCESS, ERROR_NOT_FOUND, ERROR_MISSING_ARGUMENT}
}
