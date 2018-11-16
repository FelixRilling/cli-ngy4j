package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link LookupResolver}.
 */
class LookupResolverTest {

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)}} throws an {@link IllegalArgumentException}
     * for an empty path.
     */
    @Test
    void resolveCommandReturnsNullForEmpty() {
        Executable closureContainingCodeToTest = () -> new LookupResolver(true).resolve(new CommandMap(), Collections.emptyList(), false);

        assertThrows(IllegalArgumentException.class, closureContainingCodeToTest);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)} returns a {@link LookupErrorNotFound} for non-existent commands.
     */
    @Test
    void resolveCommandReturnsLookupErrorForNotFound() {
        String commandName = "foo";

        LookupResult lookupResult = new LookupResolver(true).resolve(new CommandMap(), Collections.singletonList(commandName), false);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_NOT_FOUND);
        assertThat(((LookupErrorNotFound) lookupResult).getMissing()).isEqualTo(commandName);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)}  returns a {@link LookupErrorMissingArgs} when arguments are missing.
     */
    @Test
    void resolveCommandReturnsLookupErrorForMissingArgument() {
        String commandName = "foo";
        Argument argument = new Argument("bar", true);
        Command command = new Command(null, Collections.emptyList(), Collections.singletonList(argument));
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);

        LookupResult lookupResult = new LookupResolver(true).resolve(commandMap, Collections.singletonList(commandName), true);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_MISSING_ARGUMENT);
        assertThat(((LookupErrorMissingArgs) lookupResult).getMissing()).containsExactly(argument);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)}  returns the {@link Command}.
     */
    @Test
    void resolveCommandReturnsCommand() {
        String commandName = "foo";
        Command command = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);

        LookupResult lookupResult = new LookupResolver(true).resolve(commandMap, Collections.singletonList(commandName), false);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)}  returns dangling path elements.
     */
    @Test
    void resolveCommandReturnsDangling() {
        String pathElement1 = "foo";
        List<String> commandNames = Arrays.asList(pathElement1, "bar", "buzz");
        Command command = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandNames.get(0), command);

        LookupResult lookupResult = new LookupResolver(true).resolve(commandMap, commandNames, false);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult.getPathUsed()).containsExactly(pathElement1);
        assertThat(lookupResult.getPathDangling()).isEqualTo(commandNames.subList(1, commandNames.size()));
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)}  honors caseSensitive.
     */
    @Test
    void resolveCommandHonorsCaseSensitive() {
        String commandName = "foo";
        Command command = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);

        LookupResult lookupResultCaseSensitive = new LookupResolver(true).resolve(commandMap, Collections.singletonList("fOo"), false);
        assertThat(lookupResultCaseSensitive.getType()).isEqualTo(LookupResult.ResultType.ERROR_NOT_FOUND);

        LookupResult lookupResultCaseInsensitive = new LookupResolver(false).resolve(commandMap, Collections.singletonList("fOo"), false);
        assertThat(lookupResultCaseInsensitive.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResultCaseInsensitive).getCommand()).isEqualTo(command);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)}  resolves sub-commands.
     */
    @Test
    void resolveCommandResolvesSubCommands() {
        String commandName2 = "bar";
        Command command2 = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        String commandName1 = "foo";
        Command command1 = new Command(null, Collections.emptyList(), null, null, clingy);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);

        LookupResult lookupResult = new LookupResolver(true).resolve(commandMap1, Arrays.asList(commandName1, commandName2), false);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, boolean)}  resolves sub-command arguments.
     */
    @Test
    void resolveCommandResolvesSubCommandArguments() {
        String commandName2 = "bar";
        String argumentName = "baa";
        Argument argument = new Argument(argumentName, true);
        Command command2 = new Command(null, Collections.emptyList(), Collections.singletonList(argument));
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        String commandName1 = "foo";
        Command command1 = new Command(null, Collections.emptyList(), null, null, clingy);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);

        String argumentVal = "fizz";
        LookupResult lookupResult = new LookupResolver(true).resolve(
            commandMap1,
            Arrays.asList(commandName1, commandName2, argumentVal),
            true
        );
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
        assertThat(((LookupSuccess) lookupResult).getArgs()).containsEntry(argumentName, argumentVal);
    }
}
