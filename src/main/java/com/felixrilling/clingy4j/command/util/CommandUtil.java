package com.felixrilling.clingy4j.command.util;

import com.felixrilling.clingy4j.command.CommandMap;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.LinkedList;
import java.util.List;

public class CommandUtil {

    private CommandUtil() {
        // Hide public constructor.
    }

    public static List<String> getSimilar(CommandMap mapAliased, String name) {
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
