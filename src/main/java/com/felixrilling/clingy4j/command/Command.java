package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.command.argument.CommandArgument;
import com.felixrilling.clingy4j.command.argument.CommandArgumentMap;

import java.util.List;
import java.util.function.Function;

public class Command implements ICommand {
    private Function<CommandArgumentMap, Void> fn;
    private List<String> alias;
    private List<CommandArgument> args;
    private Object data;
    private Clingy sub;

    /**
     * @see Command#Command(Function, List, List, Object, Clingy)
     */
    public Command(Function<CommandArgumentMap, Void> fn, List<String> alias, List<CommandArgument> args) {
        this(fn, alias, args, null);
    }

    /**
     * @see Command#Command(Function, List, List, Object, Clingy)
     */
    public Command(Function<CommandArgumentMap, Void> fn, List<String> alias, List<CommandArgument> args, Object data) {
        this(fn, alias, args, data, null);
    }

    /**
     * Creates a {@link Command}
     *
     * @param fn    Function of the command, to be called with the arguments defined.
     * @param alias List of aliases to find this command by.
     * @param args  Array of arguments to use for this command.
     * @param data  Optional object providing extra info about this command.
     * @param sub   If not null, a sub-instance of {@link Clingy} for nested commands.
     */
    public Command(Function<CommandArgumentMap, Void> fn, List<String> alias, List<CommandArgument> args, Object data, Clingy sub) {
        this.fn = fn;
        this.alias = alias;
        this.args = args;
        this.data = data;
        this.sub = sub;
    }

    public Function<CommandArgumentMap, Void> getFn() {
        return fn;
    }

    public void setFn(Function<CommandArgumentMap, Void> fn) {
        this.fn = fn;
    }

    public List<String> getAlias() {
        return alias;
    }

    public List<CommandArgument> getArgs() {
        return args;
    }

    public void setArgs(List<CommandArgument> args) {
        this.args = args;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Clingy getSub() {
        return sub;
    }

    public void setSub(Clingy sub) {
        this.sub = sub;
    }
}
