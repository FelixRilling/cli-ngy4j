package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.LookupResolver.ArgumentResolving;
import com.felixrilling.clingy4j.lookup.LookupResolver.CaseSensitivity;
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
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)}} throws an {@link IllegalArgumentException}
     * for an empty path.
     */
    @Test
    void resolveCommandReturnsNullForEmpty() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new LookupResolver(CaseSensitivity.SENSITIVE).resolve(new CommandMap(), Collections.emptyList(), ArgumentResolving.IGNORE)
        );
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} returns a {@link LookupErrorNotFound} for non-existent commands.
     */
    @Test
    void resolveCommandReturnsLookupErrorForNotFound() {
        String commandName = "foo";

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(new CommandMap(), Collections.singletonList(commandName), ArgumentResolving.IGNORE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_NOT_FOUND);
        assertThat(((LookupErrorNotFound) lookupResult).getMissing()).isEqualTo(commandName);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} returns a {@link LookupErrorMissingArgs} when arguments are missing.
     */
    @Test
    void resolveCommandReturnsLookupErrorForMissingArgument() {
        String commandName = "foo";
        Argument argument = new Argument("bar", true);
        Command command = new Command(null, Collections.emptyList(), Collections.singletonList(argument));
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap, Collections.singletonList(commandName), ArgumentResolving.RESOLVE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_MISSING_ARGUMENT);
        assertThat(((LookupErrorMissingArgs) lookupResult).getMissing()).containsExactly(argument);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} returns the {@link Command}.
     */
    @Test
    void resolveCommandReturnsCommand() {
        String commandName = "foo";
        Command command = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap, Collections.singletonList(commandName), ArgumentResolving.IGNORE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} returns dangling path elements.
     */
    @Test
    void resolveCommandReturnsDangling() {
        String pathElement1 = "foo";
        List<String> commandNames = List.of(pathElement1, "bar", "buzz");
        Command command = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandNames.get(0), command);

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap, commandNames, ArgumentResolving.IGNORE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult.getPathUsed()).containsExactly(pathElement1);
        assertThat(lookupResult.getPathDangling()).isEqualTo(commandNames.subList(1, commandNames.size()));
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} honors caseSensitive.
     */
    @Test
    void resolveCommandHonorsCaseSensitive() {
        String commandName = "foo";
        Command command = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap = new CommandMap();
        commandMap.put(commandName, command);

        LookupResult lookupResultCaseSensitive = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap, Collections.singletonList("fOo"), ArgumentResolving.IGNORE);
        assertThat(lookupResultCaseSensitive.getType()).isEqualTo(LookupResult.ResultType.ERROR_NOT_FOUND);

        LookupResult lookupResultCaseInsensitive = new LookupResolver(CaseSensitivity.INSENSITIVE).resolve(commandMap, Collections.singletonList("fOo"), ArgumentResolving.IGNORE);
        assertThat(lookupResultCaseInsensitive.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResultCaseInsensitive).getCommand()).isEqualTo(command);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} resolves sub-commands.
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

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap1, List.of(commandName1, commandName2), ArgumentResolving.IGNORE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1, commandName2);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} resolves sub-command arguments.
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
        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(
            commandMap1,
            List.of(commandName1, commandName2, argumentVal),
            ArgumentResolving.RESOLVE
        );
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
        assertThat(((LookupSuccess) lookupResult).getArgs()).containsEntry(argumentName, argumentVal);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1, commandName2);
        assertThat(lookupResult.getPathDangling()).containsExactly(argumentVal);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} resolves sub-command optional arguments.
     */
    @Test
    void resolveCommandResolvesSubCommandOptionalArguments() {
        String commandName2 = "bar";
        String argumentName = "baa";
        Argument argument = new Argument(argumentName, false);
        Command command2 = new Command(null, Collections.emptyList(), Collections.singletonList(argument));
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        String commandName1 = "foo";
        Command command1 = new Command(null, Collections.emptyList(), null, null, clingy);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);

        String argumentVal = "fizz";
        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(
            commandMap1,
            List.of(commandName1, commandName2, argumentVal),
            ArgumentResolving.RESOLVE
        );
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
        assertThat(((LookupSuccess) lookupResult).getArgs()).containsEntry(argumentName, argumentVal);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1, commandName2);
        assertThat(lookupResult.getPathDangling()).containsExactly(argumentVal);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} correctly returns an non-successful result if sub-commands do.
     */
    @Test
    void resolveCommandReturnsErrorForMissingArgsOnSubCommand() {
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

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(
            commandMap1,
            List.of(commandName1, commandName2),
            ArgumentResolving.RESOLVE
        );
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_MISSING_ARGUMENT);
        assertThat(((LookupErrorMissingArgs) lookupResult).getMissing()).containsExactly(argument);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1, commandName2);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} resolves sub-commands over args when matching optionals.
     */
    @Test
    void resolveCommandResolvesSubCommandsOverAgsWhenMatchingOptionals() {
        String commandName2 = "bar";
        Command command2 = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        String commandName1 = "foo";
        Argument argument1 = new Argument("arg1", false);
        Command command1 = new Command(null, Collections.emptyList(), Collections.singletonList(argument1), null, clingy);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap1, List.of(commandName1, commandName2), ArgumentResolving.RESOLVE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1, commandName2);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} resolves sub-commands over args when matching.
     */
    @Test
    void resolveCommandResolvesSubCommandsOverAgsWhenMatching() {
        String commandName2 = "bar";
        Command command2 = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        String commandName1 = "foo";
        Argument argument1 = new Argument("arg1", true);
        Command command1 = new Command(null, Collections.emptyList(), Collections.singletonList(argument1), null, clingy);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap1, List.of(commandName1, commandName2), ArgumentResolving.RESOLVE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1, commandName2);
        assertThat(lookupResult.getPathDangling()).isEmpty();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} resolves optional args over sub-commands when not matching.
     */
    @Test
    void resolveCommandResolvesAgsOverSubCommandsWhenNotMatchingOptionals() {
        String commandName2 = "bar";
        Command command2 = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        String commandName1 = "foo";
        String argumentName1 = "arg1";
        Argument argument1 = new Argument(argumentName1, false);
        Command command1 = new Command(null, Collections.emptyList(), Collections.singletonList(argument1), null, clingy);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap1, List.of(commandName1, argumentName1), ArgumentResolving.RESOLVE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command1);
        assertThat(((LookupSuccess) lookupResult).getArgs()).containsEntry(argumentName1, argumentName1);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1);
        assertThat(lookupResult.getPathDangling()).containsExactly(argumentName1);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List, ArgumentResolving)} resolves args over sub-commands when not matching.
     */
    @Test
    void resolveCommandResolvesAgsOverSubCommandsWhenNotMatching() {
        String commandName2 = "bar";
        Command command2 = new Command(null, Collections.emptyList(), null);
        CommandMap commandMap2 = new CommandMap();
        commandMap2.put(commandName2, command2);
        Clingy clingy = new Clingy(commandMap2);

        String commandName1 = "foo";
        String argumentName1 = "arg1";
        Argument argument1 = new Argument(argumentName1, true);
        Command command1 = new Command(null, Collections.emptyList(), Collections.singletonList(argument1), null, clingy);
        CommandMap commandMap1 = new CommandMap();
        commandMap1.put(commandName1, command1);

        LookupResult lookupResult = new LookupResolver(CaseSensitivity.SENSITIVE).resolve(commandMap1, List.of(commandName1, argumentName1), ArgumentResolving.RESOLVE);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command1);
        assertThat(((LookupSuccess) lookupResult).getArgs()).containsEntry(argumentName1, argumentName1);
        assertThat(lookupResult.getPathUsed()).containsExactly(commandName1);
        assertThat(lookupResult.getPathDangling()).containsExactly(argumentName1);
    }
}
