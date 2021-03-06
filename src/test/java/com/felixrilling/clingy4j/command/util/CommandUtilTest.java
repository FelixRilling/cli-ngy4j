package com.felixrilling.clingy4j.command.util;

import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.CommandUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CommandUtil}.
 */
class CommandUtilTest {

    /**
     * Asserts that {@link CommandUtil#getSimilar(CommandMap, String)} returns a list of similar names.
     */
    @Test
    void getSimilarReturnsSimilar() {
        String commandName1 = "foo";
        String commandName2 = "bar";
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName1, new Command(null, Collections.emptyList(), Collections.emptyList()));
        commandMap.put(commandName2, new Command(null, Collections.emptyList(), Collections.emptyList()));

        assertThat(CommandUtil.getSimilar(commandMap, "foo2")).containsExactly(commandName1);
    }
}
