package com.rilling.clingy4j.argument;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ArgumentMatcher}.
 */
class ArgumentMatcherTest {

    /**
     * Asserts that {@link ArgumentMatcher} works with an empty list.
     */
    @Test
    void argumentMatcherWorksWithEmptyExpected() {
        ArgumentMatcher argumentMatcher = new ArgumentMatcher(Collections.emptyList(), Collections.emptyList());

        assertThat(argumentMatcher.getResult()).isEmpty();
        assertThat(argumentMatcher.getMissing()).isEmpty();
    }

    /**
     * Asserts that {@link ArgumentMatcher} collects found arguments.
     */
    @Test
    void argumentMatcherCollectsResults() {
        String expected = "foo";
        String provided = "bar";
        ArgumentMatcher argumentMatcher = new ArgumentMatcher(
            Collections.singletonList(new Argument(expected, true)),
            Collections.singletonList(provided)
        );

        assertThat(argumentMatcher.getResult()).containsEntry(expected, provided);
        assertThat(argumentMatcher.getMissing()).isEmpty();
    }

    /**
     * Asserts that {@link ArgumentMatcher} falls back for optional arguments.
     */
    @Test
    void argumentMatcherFallsBack() {
        String expected = "foo";
        String value = "bar";
        ArgumentMatcher argumentMatcher = new ArgumentMatcher(
            Collections.singletonList(new Argument(expected, false, value)),
            Collections.emptyList()
        );

        assertThat(argumentMatcher.getResult()).containsEntry(expected, value);
        assertThat(argumentMatcher.getMissing()).isEmpty();
    }

    /**
     * Asserts that {@link ArgumentMatcher} collects missing arguments.
     */
    @Test
    void argumentMatcherCollectsMissing() {
        String expected = "foo";
        Argument argument = new Argument(expected, true);
        ArgumentMatcher argumentMatcher = new ArgumentMatcher(
            Collections.singletonList(argument),
            Collections.emptyList()
        );

        assertThat(argumentMatcher.getResult()).isEmpty();
        assertThat(argumentMatcher.getMissing()).contains(argument);
    }

}
