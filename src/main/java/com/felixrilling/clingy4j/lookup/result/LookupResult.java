package com.felixrilling.clingy4j.lookup.result;

import com.felixrilling.clingy4j.lookup.LookupResolver;

import java.util.List;

/**
 * Abstract response of {@link LookupResolver}.
 */
public abstract class LookupResult {

    private boolean successful;
    private List<String> pathDangling;
    private List<String> pathUsed;
    private ResultType type;

    public enum ResultType {SUCCESS, ERROR_NOT_FOUND, ERROR_MISSING_ARGUMENT}

    /**
     * Creates a new {@link LookupResult}.
     *
     * @param successful   If the lookup was successful.
     * @param type         Type of the result, see {@link ResultType}.
     * @param pathDangling Dangling pathUsed.
     * @param pathUsed     Path used.
     */
    public LookupResult(boolean successful, ResultType type, List<String> pathDangling, List<String> pathUsed) {
        this.successful = successful;
        this.type = type;
        this.pathDangling = pathDangling;
        this.pathUsed = pathUsed;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public ResultType getType() {
        return type;
    }

    public List<String> getPathUsed() {
        return pathUsed;
    }

    public List<String> getPathDangling() {
        return pathDangling;
    }


}
