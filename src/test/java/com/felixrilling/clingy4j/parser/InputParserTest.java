package com.felixrilling.clingy4j.parser;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class InputParserTest {

    /**
     * Asserts that {@link InputParser} creates a matcher.
     */
    @Test
    public void InputParserCreatesPattern() {
        List<String> legalQuotes = Arrays.asList("\"", "'");
        InputParser parser = new InputParser(legalQuotes);

        assertThat(parser.getPattern()).isInstanceOf(Pattern.class);
    }
}
