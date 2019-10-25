package com.rilling.clingy4j.lookup.result;

import com.rilling.clingy4j.command.Command;

import java.util.List;
import java.util.Map;

/**
 * {@link LookupResult} indicating the lookup completed successfully.
 */
public class LookupSuccess extends LookupResult {

    private final Command command;
    private final Map<String, String> args;

    /**
     * Creates a {@link LookupSuccess}.
     *
     * @param pathDangling Dangling pathUsed.
     * @param pathUsed     Path used.
     * @param command      Command that was looked up.
     * @param args         Arguments that were looked up.
     */
    public LookupSuccess(List<String> pathDangling, List<String> pathUsed, Command command, Map<String, String> args) {
        super(true, ResultType.SUCCESS, pathDangling, pathUsed);
        this.command = command;
        this.args = args;
    }

    public Command getCommand() {
        return command;
    }

    public Map<String, String> getArgs() {
        return args;
    }
}
