package com.felixrilling.clingy4j.parser;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class InputParserTest {

    private InputParser inputParser;

    @Before
    public void before() {
        inputParser = new InputParser(Arrays.asList("\"", "'"));
    }

    /**
     * Asserts that {@link InputParser} creates a matcher pattern.
     */
    @Test
    public void inputParserCreatesPattern() {
        assertThat(inputParser.getPattern()).isInstanceOf(Pattern.class);
    }

    /**
     * Asserts that {@link InputParser} escapes special characters.
     */
    @Test
    public void inputParserEscapesSpecials() {
        InputParser inputParserSpecial = new InputParser(Arrays.asList("?", "$", "("));

        assertThat(inputParserSpecial.getPattern()).isInstanceOf(Pattern.class);
    }

    /**
     * Asserts that {@link InputParser} splits spaces from the input.
     */
    @Test
    public void parseSplitsSpaces() {
        assertThat(inputParser.parse("foo")).containsExactly("foo");
        assertThat(inputParser.parse("foo bar")).containsExactly("foo", "bar");
        assertThat(inputParser.parse("foo bar  fizz")).containsExactly("foo", "bar", "fizz");
    }

    /**
     * Asserts that {@link InputParser} honors quotes when splitting.
     */
    @Test
    public void parseHonorsQuotes() {
        assertThat(inputParser.parse("'foo bar'")).containsExactly("foo bar");
        assertThat(inputParser.parse("foo 'bar'")).containsExactly("foo", "bar");
        assertThat(inputParser.parse("foo 'bar  fizz'")).containsExactly("foo", "bar  fizz");
    }
}
