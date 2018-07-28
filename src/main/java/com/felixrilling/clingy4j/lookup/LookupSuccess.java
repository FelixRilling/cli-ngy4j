package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.command.ICommand;
import com.felixrilling.clingy4j.command.argument.ResolvedArgumentMap;

import java.util.List;

public class LookupSuccess extends LookupResult {
    private final ICommand command;
    private final ResolvedArgumentMap args;

    LookupSuccess(List<String> path, List<String> pathDangling, ICommand command, ResolvedArgumentMap args) {
        super(true, ResultType.SUCCESS, path, pathDangling);
        this.command = command;
        this.args = args;
    }

    public ICommand getCommand() {
        return command;
    }

    public ResolvedArgumentMap getArgs() {
        return args;
    }
}
