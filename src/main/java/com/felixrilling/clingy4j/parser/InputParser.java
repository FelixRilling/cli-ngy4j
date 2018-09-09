package com.felixrilling.clingy4j.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Manages parsing input strings into a path list.
 */
public class InputParser {

    private final List<String> legalQuotes;
    private final Pattern pattern;
    private final Logger logger = LoggerFactory.getLogger(InputParser.class);

    /**
     * @see InputParser#InputParser(List)
     */
    public InputParser() {
        this(Collections.singletonList("\""));
    }

    /**
     * Creates an {@link InputParser}.
     *
     * @param legalQuotes List of quotes to use when parsing strings.
     */
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

    /**
     * Parses an input string.
     *
     * @param input Input string to parse.
     * @return Path list.
     */
    public List<String> parse(String input) {
        logger.debug("Parsing input '{}'", input);
        return pattern
            .matcher(input)
            .results()
            .map(this::getBestGroupMatch)
            .collect(Collectors.toList());
    }

    private Pattern generateMatcher() {
        String matchBase = "(\\S+)";

        logger.debug("Creating matcher.");
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

        logger.debug("Successfully created matcher.");
        return result;
    }

    private String getBestGroupMatch(MatchResult matchResult) {
        // Skip first (the full-match) group and search for the next non-null group
        int i = 1;

        logger.debug("Finding best match.");

        while (i <= matchResult.groupCount()) {
            String group = matchResult.group(i);

            if (group != null) {
                logger.debug("Found match '{}'", group);
                return group;
            }

            i++;
        }

        logger.warn("Could not find any match.");
        return null;
    }
}
