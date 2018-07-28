package com.felixrilling.clingy4j.command.argument;

import java.util.List;

public class ArgumentMatcher {

    private List<Argument> missing;
    private ResolvedArgumentMap result;

    public ArgumentMatcher(List<Argument> expected, List<String> given) {
        // TODO implement this
    }

    public ResolvedArgumentMap getResult() {
        return result;
    }

    public List<Argument> getMissing() {
        return missing;
    }
}
