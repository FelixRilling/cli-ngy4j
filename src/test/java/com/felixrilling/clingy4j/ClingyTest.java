package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.ILookupResult;
import com.felixrilling.clingy4j.lookup.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.LookupSuccess;
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

    /**
     * Asserts that {@link Clingy} constructs with a sub-commands.
     */
    @Test
    public void clingyConstructsWithSubCommands() {
        String commandName1 = "foo";
        String commandName2 = "bar";

        CommandMap commandMap2 = new CommandMap();
        Command command2 = new Command(null, Lists.emptyList(), null);
        commandMap2.put(commandName2, command2);
        Clingy clingy2 = new Clingy(commandMap2);

        CommandMap commandMap1 = new CommandMap();
        Command command1 = new Command(null, Lists.emptyList(), null, null, clingy2);
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
     * Asserts that {@link Clingy#resolve(List)} returns null for an empty path.
     */
    @Test
    public void resolveCommandReturnsNullForEmpty() {
        Clingy clingy = new Clingy();

        assertThat(clingy.resolve(new ArrayList<>())).isNull();
    }

    /**
     * Asserts that {@link Clingy#resolve(List)} returns a {@link LookupErrorNotFound} for non-existent commands.
     */
    @Test
    public void resolveCommandReturnsNullForNotFound() {
        Clingy clingy = new Clingy();

        assertThat(clingy.resolve(Collections.singletonList("foo"))).isInstanceOf(LookupErrorNotFound.class);
    }

    /**
     * Asserts that {@link Clingy#resolve(List)} returns the {@link Command}.
     */
    @Test
    public void resolveCommandReturnsCommand() {
        CommandMap commandMap = new CommandMap();
        String commandName = "foo";
        Command command = new Command(null, Lists.emptyList(), null);
        commandMap.put(commandName, command);
        Clingy clingy = new Clingy(commandMap);

        ILookupResult lookupResult = clingy.resolve(Collections.singletonList(commandName));
        assertThat(lookupResult).isInstanceOf(LookupSuccess.class);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command);
    }

    /**
     * Asserts that {@link Clingy#resolve(List)} returns dangling elements.
     */
    @Test
    public void resolveCommandReturnsDangling() {
        CommandMap commandMap = new CommandMap();
        List<String> commandNames = Arrays.asList("foo", "bar", "buzz");
        Command command = new Command(null, Lists.emptyList(), null);
        commandMap.put(commandNames.get(0), command);
        Clingy clingy = new Clingy(commandMap);

        ILookupResult lookupResult = clingy.resolve(commandNames);
        assertThat(lookupResult).isInstanceOf(LookupSuccess.class);
        assertThat(lookupResult.getPathDangling()).isEqualTo(commandNames.subList(1, commandNames.size()));
    }

    /**
     * Asserts that {@link Clingy#resolve(List)} resolves sub-commands.
     */
    @Test
    public void resolveCommandResolvesSubCommands() {
        String commandName1 = "foo";
        String commandName2 = "bar";

        CommandMap commandMap2 = new CommandMap();
        Command command2 = new Command(null, Lists.emptyList(), null);
        commandMap2.put(commandName2, command2);
        Clingy clingy2 = new Clingy(commandMap2);

        CommandMap commandMap1 = new CommandMap();
        Command command1 = new Command(null, Lists.emptyList(), null, null, clingy2);
        commandMap1.put(commandName1, command1);
        Clingy clingy1 = new Clingy(commandMap1);

        ILookupResult lookupResult = clingy1.resolve(Arrays.asList(commandName1, commandName2));
        assertThat(lookupResult).isInstanceOf(LookupSuccess.class);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command1);
    }
}
