package com.felixrilling.clingy4j.lookup;

import java.util.List;

abstract class LookupResult implements ILookupResult {
    private final boolean successful;
    private final List<String> path;
    private final List<String> pathDangling;

    LookupResult(boolean successful, List<String> path, List<String> pathDangling) {
        this.successful = successful;
        this.path = path;
        this.pathDangling = pathDangling;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public List<String> getPath() {
        return path;
    }

    public List<String> getPathDangling() {
        return pathDangling;
    }
}
