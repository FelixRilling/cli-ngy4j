package com.felixrilling.clingy4j.lookup;

import java.util.List;

public abstract class LookupError extends LookupResult {
    private LookupErrorTypes errorType;

    public LookupError(List<String> path, List<String> pathDangling, LookupErrorTypes errorType) {
        super(false, path, pathDangling);
        this.errorType = errorType;
    }

    public LookupErrorTypes getErrorType() {
        return errorType;
    }
}
