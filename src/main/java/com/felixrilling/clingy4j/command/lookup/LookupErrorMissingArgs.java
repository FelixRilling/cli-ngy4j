package com.felixrilling.clingy4j.command.lookup;

import com.felixrilling.clingy4j.command.argument.CommandArgument;

import java.util.List;

public class LookupErrorMissingArgs extends LookupError {
    private List<CommandArgument> missing;

    public LookupErrorMissingArgs(List<String> path, List<String> pathDangling, List<CommandArgument> missing, List<String> similar) {
        super(path, pathDangling, LookupErrorTypes.MISSING_ARGUMENT);
        this.missing = missing;
    }

    public List<CommandArgument> getMissing() {
        return missing;
    }
}
