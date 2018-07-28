package com.felixrilling.clingy4j.command.argument;

import java.util.List;

public class CommandArgumentMatcher {

    private List<CommandArgument> missing;
    private ResolvedArgumentMap result;

    public CommandArgumentMatcher(List<CommandArgument> expected, List<String> given) {
        // TODO implement this
    }

    public ResolvedArgumentMap getResult() {
        return result;
    }

    public List<CommandArgument> getMissing() {
        return missing;
    }
}
