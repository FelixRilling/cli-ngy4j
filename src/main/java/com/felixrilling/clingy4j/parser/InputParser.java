package com.felixrilling.clingy4j.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class InputParser {
    private final List<String> legalQuotes;

    private final Pattern pattern;
    private Logger logger = LoggerFactory.getLogger(InputParser.class);

    public InputParser(List<String> legalQuotes) {
        this.legalQuotes = legalQuotes;
        this.pattern = generateMatcher();
    }

    public List<String> getLegalQuotes() {
        return legalQuotes;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public List<String> parse(String input) {
        return pattern
            .matcher(input)
            .results()
            .map(this::getBestGroupMatch)
            .collect(Collectors.toList());
    }


    private Pattern generateMatcher() {
        final String matchBase = "(\\S+)";

        List<String> matchItems = legalQuotes
            .stream()
            .map(r -> String.format("%1$s(.+?)%1$s", Pattern.quote(r)))
            .collect(Collectors.toList());

        matchItems.add(matchBase);

        Pattern result;

        try {
            result = Pattern.compile(String.join("|", matchItems));
        } catch (PatternSyntaxException e) {
            logger.error("The parsing pattern is invalid, this should never happen.", e);
            throw e;
        }

        return result;
    }


    private String getBestGroupMatch(MatchResult matchResult) {
        // Skip first (the full-match) group and search for the next non-null group
        int i = 1;

        while (i <= matchResult.groupCount()) {
            if (matchResult.group(i) != null)
                return matchResult.group(i);

            i++;
        }

        return null;
    }
}
