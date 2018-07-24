package com.felixrilling.clingy4j.parser;

import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.ILookupResult;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InputParser {
    private final List<String> legalQuotes;
    private final Pattern pattern;

    public InputParser(List<String> legalQuotes) {
        this.legalQuotes = legalQuotes;
        this.pattern = generateMatcher();
    }

    public ILookupResult parse(CommandMap mapAliased, String input) {
        List<String> split = Arrays.asList(pattern.split(input));

        return null;
    }

    public List<String> getLegalQuotes() {
        return legalQuotes;
    }

    public Pattern getPattern() {
        return pattern;
    }

    private Pattern generateMatcher() {
        List<String> patterns = legalQuotes.stream().map(r -> String.format("%1$s.+?%1$s", r)).collect(Collectors.toList());

        patterns.add("\\S+");

        String regex = String.format("/(%s)/g", String.join("|", patterns));

        return Pattern.compile(regex);
    }
}
