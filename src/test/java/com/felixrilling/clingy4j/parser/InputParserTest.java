package com.felixrilling.clingy4j.parser;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class InputParserTest {

    private final List<String> legalQuotes = Arrays.asList("\"", "'");

    /**
     * Asserts that {@link InputParser} creates a matcher.
     */
    @Test
    public void InputParserCreatesPattern() {
        InputParser parser = new InputParser(legalQuotes);

        assertThat(parser.getPattern()).isInstanceOf(Pattern.class);
    }

    /**
     * Asserts that {@link InputParser} splits spaces from the input.
     */
    @Test
    public void parseSplitsSpaces() {
        InputParser parser = new InputParser(legalQuotes);

        assertThat(parser.parse("foo")).containsExactly("foo");
        assertThat(parser.parse("foo bar")).containsExactly("foo", "bar");
        assertThat(parser.parse("foo bar  fizz")).containsExactly("foo", "bar", "fizz");
    }

    /**
     * Asserts that {@link InputParser} honors quotes when splitting.
     */
    @Test
    public void parseHonorsQuotes() {
        InputParser parser = new InputParser(legalQuotes);

        assertThat(parser.parse("'foo bar'")).containsExactly("foo bar");
        assertThat(parser.parse("foo 'bar'")).containsExactly("foo", "bar");
        assertThat(parser.parse("foo 'bar  fizz'")).containsExactly("foo", "bar  fizz");
    }
}
