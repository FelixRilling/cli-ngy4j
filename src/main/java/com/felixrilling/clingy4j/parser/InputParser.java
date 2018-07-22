package com.felixrilling.clingy4j.parser;

import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.ILookupResult;

import java.util.List;

public class InputParser {
    private List<String> legalQuotes;

    public InputParser(List<String> legalQuotes) {
        this.legalQuotes = legalQuotes;
    }

    public ILookupResult parse(CommandMap mapAliased, String input) {
        //TODO implement
        return null;
    }

    public List<String> getLegalQuotes() {
        return legalQuotes;
    }
}
