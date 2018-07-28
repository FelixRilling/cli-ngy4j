package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.result.LookupErrorMissingArgs;
import com.felixrilling.clingy4j.lookup.result.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.result.LookupResult;
import com.felixrilling.clingy4j.lookup.result.LookupSuccess;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LookupResolverTest {

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} returns null for an empty path.
     */
    @Test
    public void resolveCommandReturnsNullForEmpty() {
        assertThat(new LookupResolver().resolve(new CommandMap(), Collections.emptyList())).isNull();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} returns a {@link LookupErrorNotFound} for non-existent commands.
     */
    @Test
    public void resolveCommandReturnsLookupErrorForMissing() {
        assertThat(new LookupResolver().resolve(new CommandMap(), Collections.singletonList("foo")).getType())
            .isEqualTo(LookupResult.ResultType.ERROR_COMMAND_NOT_FOUND);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} returns the {@link Command}.
     */
    @Test
    public void resolveCommandReturnsCommand() {
        CommandMap commandMap = new CommandMap();
        String commandName = "foo";
        Command command = new Command(null, Lists.emptyList(), null);
        commandMap.put(commandName, command);

        LookupResult lookupResult = new LookupResolver().resolve(commandMap, Collections.singletonList(commandName));
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} returns dangling elements.
     */
    @Test
    public void resolveCommandReturnsDangling() {
        CommandMap commandMap = new CommandMap();
        List<String> commandNames = Arrays.asList("foo", "bar", "buzz");
        Command command = new Command(null, Lists.emptyList(), null);
        commandMap.put(commandNames.get(0), command);

        LookupResult lookupResult = new LookupResolver().resolve(commandMap, commandNames);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult.getPathDangling()).isEqualTo(commandNames.subList(1, commandNames.size()));
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} resolves sub-commands.
     */
    @Test
    public void resolveCommandResolvesSubCommands() {
        String commandName1 = "foo";
        String commandName2 = "bar";

        CommandMap commandMap2 = new CommandMap();
        Command command2 = new Command(null, Lists.emptyList(), null);
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        CommandMap commandMap1 = new CommandMap();
        Command command1 = new Command(null, Lists.emptyList(), null, null, clingy);
        commandMap1.put(commandName1, command1);

        LookupResult lookupResult = new LookupResolver().resolve(commandMap1, Arrays.asList(commandName1, commandName2));
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command1);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} honors caseSensitive.
     */
    @Test
    public void resolveCommandHonorsCaseSensitive() {
        CommandMap commandMap = new CommandMap();
        Command command = new Command(null, Lists.emptyList(), null);
        commandMap.put("foo", command);

        LookupResult lookupResultCaseSensitive = new LookupResolver(true).resolve(commandMap, Collections.singletonList("fOo"));
        assertThat(lookupResultCaseSensitive.getType()).isEqualTo(LookupResult.ResultType.ERROR_COMMAND_NOT_FOUND);

        LookupResult lookupResultCaseInsensitive = new LookupResolver(false).resolve(commandMap, Collections.singletonList("fOo"));
        assertThat(lookupResultCaseInsensitive.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResultCaseInsensitive).getCommand()).isEqualTo(command);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} returns a {@link LookupErrorMissingArgs} when arguments are missing.
     */
    @Test
    public void resolveCommandReturnsLookupErrorForMissingCommand() {
        String commandName = "foo";
        CommandMap commandMap = new CommandMap();
        Argument argument = new Argument("bar", true);
        Command command = new Command(null, Collections.emptyList(), Collections.singletonList(argument));
        commandMap.put(commandName, command);

        LookupResult lookupResult = new LookupResolver().resolve(commandMap, Collections.singletonList(commandName));
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_MISSING_ARGUMENT);
        assertThat(((LookupErrorMissingArgs) lookupResult).getMissing()).containsExactly(argument);
    }
}
