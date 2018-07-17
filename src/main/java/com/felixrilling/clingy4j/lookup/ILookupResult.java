package com.felixrilling.clingy4j.lookup;

import java.util.List;

public interface ILookupResult {
    boolean success = false;
    List<String> path = null;
    List<String> pathDangling = null;

    boolean isSuccess();

    List<String> getPath();

    List<String> getPathDangling();
}
