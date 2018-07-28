package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.command.argument.Argument;

import java.util.List;

public class LookupErrorMissingArgs extends LookupResult {
    private final List<Argument> missing;

    LookupErrorMissingArgs(List<String> path, List<String> pathDangling, List<Argument> missing) {
        super(false, ResultType.ERROR_MISSING_ARGUMENT, path, pathDangling);
        this.missing = missing;
    }

    public List<Argument> getMissing() {
        return missing;
    }
}
