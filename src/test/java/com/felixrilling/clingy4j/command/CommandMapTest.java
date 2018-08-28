package com.felixrilling.clingy4j.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CommandMap}.
 */
class CommandMapTest {

    /**
     * Asserts that {@link CommandMap#containsKeyIgnoreCase(String)} checks keys.
     */
    @Test
    void containsKeyIgnoreCaseChecksKeys() {
        CommandMap commandMap = new CommandMap();
        Command command = new Command(null, null, null);
        commandMap.put("foo", command);

        assertThat(commandMap.containsKeyIgnoreCase("foo")).isTrue();
        assertThat(commandMap.containsKeyIgnoreCase("foO")).isTrue();
        assertThat(commandMap.containsKeyIgnoreCase("bar")).isFalse();
    }

    /**
     * Asserts that {@link CommandMap#getIgnoreCase(String)} checks keys.
     */
    @Test
    void getIgnoreCaseChecksKeys() {
        CommandMap commandMap = new CommandMap();
        Command command = new Command(null, null, null);
        commandMap.put("foo", command);

        assertThat(commandMap.getIgnoreCase("foo")).isEqualTo(command);
        assertThat(commandMap.getIgnoreCase("foO")).isEqualTo(command);
        assertThat(commandMap.getIgnoreCase("bar")).isEqualTo(null);
    }
}
