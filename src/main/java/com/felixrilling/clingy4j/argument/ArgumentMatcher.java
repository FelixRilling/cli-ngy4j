package com.felixrilling.clingy4j.argument;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatcher {

    private List<Argument> missing;
    private ResolvedArgumentMap result;

    public ArgumentMatcher(List<Argument> expected, List<String> provided) {
        missing = new LinkedList<>();
        result = new ResolvedArgumentMap(expected.size());

        for (int i = 0; i < expected.size(); i++) {
            Argument expectedArg = expected.get(i);

            if (i < provided.size())
                result.put(expectedArg.getName(), provided.get(i));
            else if (!expectedArg.isRequired())
                result.put(expectedArg.getName(), expectedArg.getDefaultValue());
            else
                missing.add(expectedArg);
        }
    }

    public ResolvedArgumentMap getResult() {
        return result;
    }

    public List<Argument> getMissing() {
        return missing;
    }
}
