package com.felixrilling.clingy4j.lookup;

import java.util.List;

public interface ILookupResult {
    boolean successful = false;
    List<String> path = null;
    List<String> pathDangling = null;

    boolean isSuccessful();

    List<String> getPath();

    List<String> getPathDangling();
}
