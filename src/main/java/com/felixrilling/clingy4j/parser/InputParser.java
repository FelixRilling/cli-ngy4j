package com.felixrilling.clingy4j.parser;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InputParser {
    private final List<String> legalQuotes;
    private final Pattern pattern;

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
            .map(MatchResult::group)
            .collect(Collectors.toList());
    }

    private Pattern generateMatcher() {
        List<String> patterns = legalQuotes
            .stream()
            .map(r -> String.format("%1$s.+?%1$s", r))
            .collect(Collectors.toList());

        patterns.add("\\S+");

        String regex = String.format("/(%s)/g", String.join("|", patterns));

        return Pattern.compile(regex);
    }
}
