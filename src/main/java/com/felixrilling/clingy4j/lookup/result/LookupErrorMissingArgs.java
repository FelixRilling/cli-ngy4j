package com.felixrilling.clingy4j.lookup.result;

import com.felixrilling.clingy4j.argument.Argument;

import java.util.List;

/**
 * {@link LookupResult} indicating arguments were expected but not found.
 */
public class LookupErrorMissingArgs extends LookupResult {

    private final List<Argument> missing;

    /**
     * Creates a {@link LookupErrorMissingArgs}.
     *
     * @param path         Path used.
     * @param pathDangling Dangling path.
     * @param missing      List of missing arguments.
     */
    public LookupErrorMissingArgs(List<String> path, List<String> pathDangling, List<Argument> missing) {
        super(false, ResultType.ERROR_MISSING_ARGUMENT, path, pathDangling);
        this.missing = missing;
    }

    public List<Argument> getMissing() {
        return missing;
    }
}
