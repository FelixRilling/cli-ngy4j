package com.felixrilling.clingy4j;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ClingyTest {

    /**
     * Asserts that {@link Clingy} constructs without parameters.
     */
    @Test
    public void ClingyConstructsWithoutCommands() {
        Clingy clingy = new Clingy();

        assertThat(clingy).isNotNull();
    }

    /**
     * Asserts that {@link Clingy} constructs with a {@link CommandMap}.
     */
    @Test
    public void ClingyConstructsWithCommands() {
        CommandMap commandMap = new CommandMap();
        String commandName = "foo";
        Command command = new Command(null, Lists.emptyList(), null, null, null);
        commandMap.put(commandName, command);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.getCommand(commandName)).isSameAs(command);
    }

    /**
     * Asserts that {@link Clingy} updates the internal aliased map.
     */
    @Test
    public void ClingyUpdatesAliasedMap() {
        CommandMap commandMap = new CommandMap();
        String commandName = "foo";
        String alias1 = "bar";
        String alias2 = "fizz";
        Command command = new Command(null, Arrays.asList(alias1, alias2), null, null, null);
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
    public void ClingyUpdatesAliasedMapSkipsDuplicateKeys() {
        CommandMap commandMap = new CommandMap();
        String commandName1 = "foo";
        String commandName2 = "bar";
        String alias1 = "fizz";
        Command command1 = new Command(null, Collections.singletonList(alias1), null, null, null);
        Command command2 = new Command(null, Collections.singletonList(commandName1), null, null, null);
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
    public void ClingyUpdatesAliasedMapUpdatesAfterChanges() {
        CommandMap commandMap = new CommandMap();
        String commandName1 = "foo";
        String commandName2 = "bar";
        String alias1 = "fizz";
        Command command1 = new Command(null, Collections.singletonList(alias1), null, null, null);
        Command command2 = new Command(null, Collections.singletonList(commandName1), null, null, null);
        Clingy clingy = new Clingy(commandMap);

        clingy.setCommand(commandName1, command1);
        clingy.setCommand(commandName2, command2);

        assertThat(clingy.getCommand(commandName1)).isSameAs(command1);
        assertThat(clingy.getCommand(alias1)).isSameAs(command1);
        assertThat(clingy.getCommand(commandName2)).isSameAs(command2);
    }

}