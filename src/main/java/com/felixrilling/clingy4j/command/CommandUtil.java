package com.felixrilling.clingy4j.command;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Miscellaneous utility methods for {@link Command} and {@link CommandMap}.
 */
public class CommandUtil {

    private CommandUtil() {
        // Hide public constructor.
    }

    /**
     * Gets similar keys of a key based on their string distance.
     *
     * @param mapAliased Map to use for lookup.
     * @param name       Key to use.
     * @return List of similar keys.
     */
    @NotNull
    public static List<String> getSimilar(@NotNull CommandMap mapAliased, @NotNull String name) {
        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
        List<String> results = new LinkedList<>();
        int editDistanceMax = Integer.MAX_VALUE;

        for (String key : mapAliased.keySet()) {
            int editDistance = levenshteinDistance.apply(name, key);

            if (editDistance < editDistanceMax) {
                results.clear();
                editDistanceMax = editDistance;
            }

            if (editDistance == editDistanceMax)
                results.add(key);
        }

        return results;
    }
}
