package com.felixrilling.clingy4j.lookup;

import java.util.List;

abstract class LookupResult implements ILookupResult {
    private final boolean success;
    private final List<String> path;
    private final List<String> pathDangling;

    LookupResult(boolean success, List<String> path, List<String> pathDangling) {
        this.success = success;
        this.path = path;
        this.pathDangling = pathDangling;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getPath() {
        return path;
    }

    public List<String> getPathDangling() {
        return pathDangling;
    }
}
