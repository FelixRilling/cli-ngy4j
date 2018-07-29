package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.argument.ResolvedArgumentMap;

import java.util.List;
import java.util.function.Consumer;

/**
 * Command interface. Base implementation is {@link Command}.
 */
public interface ICommand {

    Consumer<ResolvedArgumentMap> getFn();

    void setFn(Consumer<ResolvedArgumentMap> fn);

    List<String> getAlias();

    List<Argument> getArgs();

    void setArgs(List<Argument> args);

    Object getData();

    void setData(Object data);

    Clingy getSub();

    void setSub(Clingy sub);
}
