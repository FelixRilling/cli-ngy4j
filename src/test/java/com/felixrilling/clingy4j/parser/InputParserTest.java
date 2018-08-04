package com.felixrilling.clingy4j.parser;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link InputParser}.
 */
class InputParserTest {

    /**
     * Asserts that {@link InputParser} creates a matcher pattern.
     */
    @Test
    void inputParserCreatesPattern() {
        assertThat(new InputParser().getPattern()).isInstanceOf(Pattern.class);
    }

    /**
     * Asserts that {@link InputParser} escapes special characters.
     */
    @Test
    void inputParserEscapesSpecials() {
        assertThat(new InputParser(Arrays.asList("?", "$", "(")).getPattern()).isInstanceOf(Pattern.class);
    }

    /**
     * Asserts that {@link InputParser} splits spaces from the input.
     */
    @Test
    void parseSplitsSpaces() {
        InputParser inputParser = new InputParser(Arrays.asList("\"", "'"));

        assertThat(inputParser.parse("foo")).containsExactly("foo");
        assertThat(inputParser.parse("foo bar")).containsExactly("foo", "bar");
        assertThat(inputParser.parse("foo bar  fizz")).containsExactly("foo", "bar", "fizz");
    }

    /**
     * Asserts that {@link InputParser} honors quotes when splitting.
     */
    @Test
    void parseHonorsQuotes() {
        InputParser inputParser = new InputParser(Arrays.asList("\"", "'"));

        assertThat(inputParser.parse("'foo bar'")).containsExactly("foo bar");
        assertThat(inputParser.parse("foo 'bar'")).containsExactly("foo", "bar");
        assertThat(inputParser.parse("foo 'bar  fizz'")).containsExactly("foo", "bar  fizz");
    }
}
