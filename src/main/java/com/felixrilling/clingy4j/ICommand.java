package com.felixrilling.clingy4j;

import java.util.List;
import java.util.function.Function;

public interface ICommand {
    Function<String[], Void> fn = null;
    List<String> alias = null;
    ClingyArgument[] args = new ClingyArgument[0];
    Object data = null;
    Clingy sub = null;

    Function<String[], Void> getFn();

    void setFn(Function<String[], Void> fn);

    List<String> getAlias();

    void setAlias(List<String> alias);

    ClingyArgument[] getArgs();

    void setArgs(ClingyArgument[] args);

    Object getData();

    void setData(Object data);

    Clingy getSub();

    void setSub(Clingy sub);
}
