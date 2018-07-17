package com.felixrilling.clingy4j.lookup;

import java.util.List;

abstract class LookupError extends LookupResult {
    private final LookupErrorTypes errorType;

    LookupError(List<String> path, List<String> pathDangling, LookupErrorTypes errorType) {
        super(false, path, pathDangling);
        this.errorType = errorType;
    }

    public LookupErrorTypes getErrorType() {
        return errorType;
    }
}
