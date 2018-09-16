package com.felixrilling.clingy4j.lookup.result;

import com.felixrilling.clingy4j.argument.ResolvedArgumentMap;
import com.felixrilling.clingy4j.command.Command;

import java.util.List;

/**
 * {@link LookupResult} indicating the lookup completed successfully.
 */
public class LookupSuccess extends LookupResult {

    private Command command;
    private ResolvedArgumentMap args;

    /**
     * Creates a {@link LookupSuccess}.
     *
     * @param pathDangling Dangling pathUsed.
     * @param pathUsed     Path used.
     * @param command      Command that was looked up.
     * @param args         Arguments that were looked up.
     */
    public LookupSuccess(List<String> pathDangling, List<String> pathUsed, Command command, ResolvedArgumentMap args) {
        super(true, ResultType.SUCCESS, pathDangling, pathUsed);
        this.command = command;
        this.args = args;
    }

    public Command getCommand() {
        return command;
    }

    public ResolvedArgumentMap getArgs() {
        return args;
    }
}
