package com.felixrilling.clingy4j.command.lookup;

import java.util.List;

abstract class LookupResult {
    private boolean success;
    private List<String> path;
    private List<String> pathDangling;

    public LookupResult(boolean success, List<String> path, List<String> pathDangling) {
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
