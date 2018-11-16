package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.argument.Argument;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base {@link Command}.
 */
public class Command {

    private final List<String> alias;
    private final Consumer<HashMap<String, String>> fn;
    private final List<Argument> args;
    private final Object data;
    private final Clingy sub;

    /**
     * @see Command#Command(Consumer, List, List, Object, Clingy)
     */
    public Command(Consumer<HashMap<String, String>> fn, List<String> alias, List<Argument> args) {
        this(fn, alias, args, null);
    }

    /**
     * @see Command#Command(Consumer, List, List, Object, Clingy)
     */
    @SuppressWarnings("WeakerAccess")
    public Command(Consumer<HashMap<String, String>> fn, List<String> alias, List<Argument> args, Object data) {
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
    public Command(Consumer<HashMap<String, String>> fn, List<String> alias, List<Argument> args, Object data, Clingy sub) {
        this.fn = fn;
        this.alias = alias;
        this.args = args;
        this.data = data;
        this.sub = sub;
    }

    public List<String> getAlias() {
        return alias;
    }

    @SuppressWarnings("unused")
    public Consumer<HashMap<String, String>> getFn() {
        return fn;
    }

    public List<Argument> getArgs() {
        return args;
    }

    @SuppressWarnings("unused")
    public Object getData() {
        return data;
    }

    public Clingy getSub() {
        return sub;
    }
}
