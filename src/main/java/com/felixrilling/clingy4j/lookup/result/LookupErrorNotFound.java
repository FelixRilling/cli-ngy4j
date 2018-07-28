package com.felixrilling.clingy4j.lookup.result;

import java.util.List;

public class LookupErrorNotFound extends LookupResult {
    private final String missing;
    private final List<String> similar;

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
