package com.felixrilling.clingy4j.argument;

import com.felixrilling.clingy4j.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Orchestrates mapping of {@link Argument}s to user-provided input.
 */
public class ArgumentMatcher {

    private final Logger logger = LoggerFactory.getLogger(ArgumentMatcher.class);
    private final List<Argument> missing;
    private final ResolvedArgumentMap result;

    /**
     * Matches a list of {@link Argument}s to a list of string input arguments.
     *
     * @param expected {@link Argument} list of a {@link Command}
     * @param provided List of user-provided arguments.
     */
    public ArgumentMatcher(List<Argument> expected, List<String> provided) {
        missing = new LinkedList<>();
        result = new ResolvedArgumentMap(expected.size());

        logger.debug("Matching arguments {} with {}", expected, provided);

        for (int i = 0; i < expected.size(); i++) {
            Argument expectedArg = expected.get(i);

            if (i < provided.size()) {
                logger.trace("Found matching argument for {}, adding to result: {}", expectedArg.getName(), provided.get(i));
                result.put(expectedArg.getName(), provided.get(i));
            } else if (!expectedArg.isRequired()) {
                logger.trace("No matching argument found for {}, using default: {}", expectedArg.getName(), expectedArg.getDefaultValue());
                result.put(expectedArg.getName(), expectedArg.getDefaultValue());
            } else {
                logger.trace("No matching argument found for {}, adding to missing.", expectedArg.getName());
                missing.add(expectedArg);
            }
        }

        logger.debug("Finished matching arguments: {} expected, {} found and {} missing.", expected.size(), result.size(), missing.size());
    }

    public ResolvedArgumentMap getResult() {
        return result;
    }

    public List<Argument> getMissing() {
        return missing;
    }
}
