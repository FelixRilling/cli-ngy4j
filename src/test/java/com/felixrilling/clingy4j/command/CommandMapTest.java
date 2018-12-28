package com.felixrilling.clingy4j.command;

import com.felixrilling.clingy4j.lookup.CaseSensitivity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CommandMap}.
 */
class CommandMapTest {

    /**
     * Asserts that {@link CommandMap#containsCommandKey(String, CaseSensitivity)} checks keys.
     */
    @Test
    void containsKeyIgnoreCaseChecksKeys() {
        CommandMap commandMap = new CommandMap();
        Command command = new Command(null, null, null);
        commandMap.put("foo", command);

        assertThat(commandMap.containsCommandKey("foo", CaseSensitivity.INSENSITIVE)).isTrue();
        assertThat(commandMap.containsCommandKey("foO", CaseSensitivity.INSENSITIVE)).isTrue();
        assertThat(commandMap.containsCommandKey("bar", CaseSensitivity.INSENSITIVE)).isFalse();
    }

    /**
     * Asserts that {@link CommandMap#getCommand(String, CaseSensitivity)} checks keys.
     */
    @Test
    void getIgnoreCaseChecksKeys() {
        CommandMap commandMap = new CommandMap();
        Command command = new Command(null, null, null);
        commandMap.put("foo", command);

        assertThat(commandMap.getCommand("foo", CaseSensitivity.INSENSITIVE)).isEqualTo(command);
        assertThat(commandMap.getCommand("foO", CaseSensitivity.INSENSITIVE)).isEqualTo(command);
        assertThat(commandMap.getCommand("bar", CaseSensitivity.INSENSITIVE)).isEqualTo(null);
    }
}
