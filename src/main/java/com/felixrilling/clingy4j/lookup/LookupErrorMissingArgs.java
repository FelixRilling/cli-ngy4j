package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.command.argument.CommandArgument;

import java.util.List;

public class LookupErrorMissingArgs extends LookupError {
    private final List<CommandArgument> missing;

    LookupErrorMissingArgs(List<String> path, List<String> pathDangling, List<CommandArgument> missing, List<String> similar) {
        super(path, pathDangling, LookupErrorTypes.MISSING_ARGUMENT);
        this.missing = missing;
    }

    public List<CommandArgument> getMissing() {
        return missing;
    }
}
