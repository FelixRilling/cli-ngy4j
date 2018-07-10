package com.felixrilling.clingy4j;

import java.util.List;
import java.util.function.Function;

public class Command implements ICommand {
    private Function<String[], Void> fn;
    private List<String> alias;
    private ClingyArgument[] args;
    private Object data;
    private Clingy sub;

    public Command(Function<String[], Void> fn, List<String> alias, ClingyArgument[] args, Object data, Clingy sub) {
        this.fn = fn;
        this.alias = alias;
        this.args = args;
        this.data = data;
        this.sub = sub;
    }

    public Function<String[], Void> getFn() {
        return fn;
    }

    public void setFn(Function<String[], Void> fn) {
        this.fn = fn;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public ClingyArgument[] getArgs() {
        return args;
    }

    public void setArgs(ClingyArgument[] args) {
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
