package com.felixrilling.clingy4j.lookup;

import java.util.List;

public class LookupErrorNotFound extends LookupError {
    private String missing;
    /* private List<String> similar;*/

    public LookupErrorNotFound(List<String> path, List<String> pathDangling, String missing /*,List<String> similar*/) {
        super(path, pathDangling, LookupErrorTypes.COMMAND_NOT_FOUND);
        this.missing = missing;
        /*  this.similar = similar;*/
    }

    public String getMissing() {
        return missing;
    }

/*    public List<String> getSimilar() {
        return similar;
    }*/
}