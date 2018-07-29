package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Clingy}.
 */
public class ClingyTest {

    /**
     * Asserts that {@link Clingy} constructs with a {@link CommandMap}.
     */
    @Test
    public void clingyConstructsWithCommands() {
        String commandName = "foo";
        Command command = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.getCommand(commandName)).isSameAs(command);
    }

    /**
     * Asserts that {@link Clingy} constructs with a sub-commands.
     */
    @Test
    public void clingyConstructsWithSubCommands() {
        String commandName2 = "bar";
        Command command2 = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy2 = new Clingy(commandMap2);

        String commandName1 = "foo";
        Command command1 = new Command(null, Collections.emptyList(), null, null, clingy2);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);
        Clingy clingy1 = new Clingy(commandMap1);

        assertThat(clingy1.getCommand(commandName1)).isSameAs(command1);
        assertThat(clingy1.getCommand(commandName1).getSub().getCommand(commandName2)).isSameAs(command2);
    }


    /**
     * Asserts that {@link Clingy} updates the internal aliased map.
     */
    @Test
    public void clingyUpdatesAliasedMap() {
        String commandName = "foo";
        String alias1 = "bar";
        String alias2 = "fizz";
        Command command = new Command(null, Arrays.asList(alias1, alias2), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.getCommand(commandName)).isSameAs(command);
        assertThat(clingy.getCommand(alias1)).isSameAs(command);
        assertThat(clingy.getCommand(alias2)).isSameAs(command);
    }

    /**
     * Asserts that {@link Clingy} updates the internal aliased map while skipping duplicate keys.
     */
    @Test
    public void clingyUpdatesAliasedMapSkipsDuplicateKeys() {
        String commandName1 = "foo";
        String commandName2 = "bar";
        String alias1 = "fizz";
        Command command1 = new Command(null, Collections.singletonList(alias1), null);
        Command command2 = new Command(null, Collections.singletonList(commandName1), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName1, command1);
        commandMap.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.getCommand(commandName1)).isSameAs(command1);
        assertThat(clingy.getCommand(alias1)).isSameAs(command1);
        assertThat(clingy.getCommand(commandName2)).isSameAs(command2);
    }

    /**
     * Asserts that {@link Clingy} updates the internal aliased map when modified after construction.
     */
    @Test
    public void clingyUpdatesAliasedMapUpdatesAfterChanges() {
        String commandName1 = "foo";
        String commandName2 = "bar";
        String alias1 = "fizz";
        Command command1 = new Command(null, Collections.singletonList(alias1), null);
        Command command2 = new Command(null, Collections.singletonList(commandName1), null);
        CommandMap commandMap = new CommandMap();
        Clingy clingy = new Clingy(commandMap);

        clingy.setCommand(commandName1, command1);
        clingy.setCommand(commandName2, command2);

        assertThat(clingy.getCommand(commandName1)).isSameAs(command1);
        assertThat(clingy.getCommand(alias1)).isSameAs(command1);
        assertThat(clingy.getCommand(commandName2)).isSameAs(command2);
    }

}
