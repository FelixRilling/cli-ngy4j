package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.argument.ResolvedArgumentMap;

import java.util.List;
import java.util.function.Consumer;

/**
 * Base {@link Command}.
 */
public class Command {

    private List<String> alias;
    private Consumer<ResolvedArgumentMap> fn;
    private List<Argument> args;
    private Object data;
    private Clingy sub;

    /**
     * @see Command#Command(Consumer, List, List, Object, Clingy)
     */
    public Command(Consumer<ResolvedArgumentMap> fn, List<String> alias, List<Argument> args) {
        this(fn, alias, args, null);
    }

    /**
     * @see Command#Command(Consumer, List, List, Object, Clingy)
     */
    public Command(Consumer<ResolvedArgumentMap> fn, List<String> alias, List<Argument> args, Object data) {
        this(fn, alias, args, data, null);
    }

    /**
     * Creates a {@link Command}
     *
     * @param fn    Function of the command, to be called with the arguments defined.
     * @param alias List of aliases to find this command by.
     * @param args  List of arguments to use for this command.
     * @param data  Optional object providing additional information about this command.
     * @param sub   If not null, a sub-instance of {@link Clingy} for nested commands.
     */
    public Command(Consumer<ResolvedArgumentMap> fn, List<String> alias, List<Argument> args, Object data, Clingy sub) {
        this.fn = fn;
        this.alias = alias;
        this.args = args;
        this.data = data;
        this.sub = sub;
    }

    public Consumer<ResolvedArgumentMap> getFn() {
        return fn;
    }

    public void setFn(Consumer<ResolvedArgumentMap> fn) {
        this.fn = fn;
    }

    public List<String> getAlias() {
        return alias;
    }

    public List<Argument> getArgs() {
        return args;
    }

    public void setArgs(List<Argument> args) {
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
