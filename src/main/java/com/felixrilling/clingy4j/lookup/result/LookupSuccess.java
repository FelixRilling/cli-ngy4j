package com.felixrilling.clingy4j.lookup.result;

import com.felixrilling.clingy4j.argument.ResolvedArgumentMap;
import com.felixrilling.clingy4j.command.ICommand;

import java.util.List;

/**
 * {@link LookupResult} indicating the lookup completed successfully.
 */
public class LookupSuccess extends LookupResult {

    private final ICommand command;
    private final ResolvedArgumentMap args;

    /**
     * Creates a {@link LookupSuccess}.
     *
     * @param path         Path used.
     * @param pathDangling Dangling path.
     * @param command      Command that was looked up.
     * @param args         Arguments that were looked up.
     */
    public LookupSuccess(List<String> path, List<String> pathDangling, ICommand command, ResolvedArgumentMap args) {
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
