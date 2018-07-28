package com.felixrilling.clingy4j.lookup.result;

import com.felixrilling.clingy4j.argument.Argument;

import java.util.List;

public class LookupErrorMissingArgs extends LookupResult {
    private final List<Argument> missing;

    public LookupErrorMissingArgs(List<String> path, List<String> pathDangling, List<Argument> missing) {
        super(false, ResultType.ERROR_MISSING_ARGUMENT, path, pathDangling);
        this.missing = missing;
    }

    public List<Argument> getMissing() {
        return missing;
    }
}
