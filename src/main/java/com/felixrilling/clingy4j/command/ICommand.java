package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.command.argument.Argument;
import com.felixrilling.clingy4j.command.argument.ResolvedArgumentMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface ICommand {
    Function<ResolvedArgumentMap, Void> fn = null;
    List<String> alias = null;
    List<Argument> args = new ArrayList<>();
    Object data = null;
    Clingy sub = null;

    Function<ResolvedArgumentMap, Void> getFn();

    void setFn(Function<ResolvedArgumentMap, Void> fn);

    List<String> getAlias();

    List<Argument> getArgs();

    void setArgs(List<Argument> args);

    Object getData();

    void setData(Object data);

    Clingy getSub();

    void setSub(Clingy sub);
}
