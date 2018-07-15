package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClingyTest {

    /**
     * Asserts that {@link Clingy} constructs without parameters.
     */
    @Test
    public void clingyConstructsWithoutCommands() {
        Clingy clingy = new Clingy();

        assertThat(clingy).isNotNull();
    }

    /**
     * Asserts that {@link Clingy} constructs with a {@link CommandMap}.
     */
    @Test
    public void clingyConstructsWithCommands() {
        CommandMap commandMap = new CommandMap();
        String commandName = "foo";
        Command command = new Command(null, Lists.emptyList(), null);
        commandMap.put(commandName, command);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.getCommand(commandName)).isSameAs(command);
    }

    // TODO: sub-instance test

    /**
     * Asserts that {@link Clingy} updates the internal aliased map.
     */
    @Test
    public void clingyUpdatesAliasedMap() {
        CommandMap commandMap = new CommandMap();
        String commandName = "foo";
        String alias1 = "bar";
        String alias2 = "fizz";
        Command command = new Command(null, Arrays.asList(alias1, alias2), null);
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
        CommandMap commandMap = new CommandMap();
        String commandName1 = "foo";
        String commandName2 = "bar";
        String alias1 = "fizz";
        Command command1 = new Command(null, Collections.singletonList(alias1), null);
        Command command2 = new Command(null, Collections.singletonList(commandName1), null);
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
        CommandMap commandMap = new CommandMap();
        String commandName1 = "foo";
        String commandName2 = "bar";
        String alias1 = "fizz";
        Command command1 = new Command(null, Collections.singletonList(alias1), null);
        Command command2 = new Command(null, Collections.singletonList(commandName1), null);
        Clingy clingy = new Clingy(commandMap);

        clingy.setCommand(commandName1, command1);
        clingy.setCommand(commandName2, command2);

        assertThat(clingy.getCommand(commandName1)).isSameAs(command1);
        assertThat(clingy.getCommand(alias1)).isSameAs(command1);
        assertThat(clingy.getCommand(commandName2)).isSameAs(command2);
    }

    /**
     * Asserts that {@link Clingy#resolveCommand(List)} returns null for an empty path.
     */
    @Test
    public void resolveCommandReturnsNullForEmpty() {
        Clingy clingy = new Clingy();

        assertThat(clingy.resolveCommand(new ArrayList<>())).isNull();
    }

    /**
     * Asserts that {@link Clingy#resolveCommand(List)} returns null for path that cannot be found.
     */
    @Test
    public void resolveCommandReturnsNullForNotFound() {
        Clingy clingy = new Clingy();

        assertThat(clingy.resolveCommand(Collections.singletonList("foo"))).isNull();
    }

    /**
     * Asserts that {@link Clingy#resolveCommand(List)} returns the {@link Command}.
     */
    @Test
    public void resolveCommandReturnsCommand() {
        CommandMap commandMap = new CommandMap();
        String commandName = "foo";
        Command command = new Command(null, Lists.emptyList(), null);
        commandMap.put(commandName, command);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.resolveCommand(Collections.singletonList(commandName))).isEqualTo(command);
    }

}
