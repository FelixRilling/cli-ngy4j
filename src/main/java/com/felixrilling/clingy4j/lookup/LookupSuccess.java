package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.command.ICommand;
import com.felixrilling.clingy4j.command.argument.CommandArgumentMap;

import java.util.List;

public class LookupSuccess extends LookupResult {
    private ICommand command;
    private CommandArgumentMap args;

    public LookupSuccess(List<String> path, List<String> pathDangling, ICommand command, CommandArgumentMap args) {
        super(true, path, pathDangling);
        this.command = command;
        this.args = args;
    }

    public ICommand getCommand() {
        return command;
    }

    public CommandArgumentMap getArgs() {
        return args;
    }
}
