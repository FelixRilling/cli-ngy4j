package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.command.argument.CommandArgument;
import com.felixrilling.clingy4j.command.argument.CommandArgumentMap;

import java.util.List;
import java.util.function.Function;

public interface ICommand {
    Function<CommandArgumentMap, Void> fn = null;
    List<String> alias = null;
    CommandArgument[] args = new CommandArgument[0];
    Object data = null;
    Clingy sub = null;

    Function<CommandArgumentMap, Void> getFn();

    void setFn(Function<CommandArgumentMap, Void> fn);

    List<String> getAlias();

    void setAlias(List<String> alias);

    CommandArgument[] getArgs();

    void setArgs(CommandArgument[] args);

    Object getData();

    void setData(Object data);

    Clingy getSub();

    void setSub(Clingy sub);
}
