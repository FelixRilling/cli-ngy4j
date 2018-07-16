package com.felixrilling.clingy4j.command.lookup;

import java.util.List;

public class LookupErrorNotFound extends LookupError {
    private List<String> missing;
    private List<String> similar;

    public LookupErrorNotFound(List<String> path, List<String> pathDangling, List<String> missing, List<String> similar) {
        super(path, pathDangling, LookupErrorTypes.COMMAND_NOT_FOUND);
        this.missing = missing;
        this.similar = similar;
    }

    public List<String> getMissing() {
        return missing;
    }

    public List<String> getSimilar() {
        return similar;
    }
}
