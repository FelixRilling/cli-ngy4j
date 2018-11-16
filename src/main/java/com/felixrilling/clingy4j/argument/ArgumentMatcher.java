package com.felixrilling.clingy4j.argument;

import com.felixrilling.clingy4j.command.Command;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Orchestrates mapping of {@link Argument}s to user-provided input.
 */
public class ArgumentMatcher {

    private static final Logger logger = LoggerFactory.getLogger(ArgumentMatcher.class);

    private final List<Argument> missing;
    private final HashMap<String, String> result;

    /**
     * Matches a list of {@link Argument}s to a list of string input arguments.
     *
     * @param expected {@link Argument} list of a {@link Command}
     * @param provided List of user-provided arguments.
     */
    public ArgumentMatcher(@NotNull List<Argument> expected, @NotNull List<String> provided) {
        missing = new LinkedList<>();
        int initialCapacity = expected.size();
        result = new HashMap<>(initialCapacity);

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

    @NotNull
    public Map<String, String> getResult() {
        return result;
    }

    @NotNull
    public List<Argument> getMissing() {
        return missing;
    }
}
