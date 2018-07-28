package com.felixrilling.clingy4j.lookup;

import java.util.List;

public abstract class LookupResult {
    private final boolean successful;
    private final List<String> path;
    private final List<String> pathDangling;
    private final ResultType type;


    public enum ResultType {SUCCESS, ERROR_COMMAND_NOT_FOUND, ERROR_MISSING_ARGUMENT}

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
}
