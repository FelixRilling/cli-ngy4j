package com.felixrilling.clingy4j.parser;

import com.felixrilling.clingy4j.lookup.ILookupResult;

import java.util.Collections;
import java.util.List;

public class InputParser {
    private List<String> legalQuotes;

    public InputParser() {
        this(Collections.singletonList("\""));
    }

    public InputParser(List<String> legalQuotes) {
        this.legalQuotes = legalQuotes;
    }

    public ILookupResult parse(String input) {
        //TODO implement
        return null;
    }
}
