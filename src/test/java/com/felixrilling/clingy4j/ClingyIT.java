package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.lookup.result.LookupErrorMissingArgs;
import com.felixrilling.clingy4j.lookup.result.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.result.LookupResult;
import com.felixrilling.clingy4j.lookup.result.LookupSuccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for example {@link Clingy} usage.
 */
class ClingyIT {

    private Clingy clingy;
    private Argument argument1;
    private Command command1;
    private Command command2;

    @BeforeEach
    void setup() {
        Map<String, Command> commandMap = new HashMap<>();

        argument1 = new Argument("val", true);
        command1 = new Command(
            System.out::println,
            Arrays.asList("fizz", "fuu"),
            Collections.singletonList(argument1)
        );
        commandMap.put("foo", command1);

        command2 = new Command(
            System.out::println,
            Collections.singletonList("baa"),
            Collections.emptyList()
        );
        commandMap.put("bar", command2);

        clingy = new Clingy(commandMap);
    }

    /**
     * Asserts that lookup of commands with args works.
     */
    @Test
    void commandNormal() {
        String input = "foo 123";
        LookupResult lookupResult = clingy.parse(input);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult.getPathDangling()).containsExactly("123");
        assertThat(lookupResult.getPathUsed()).containsExactly("foo");
        assertThat(((LookupSuccess) lookupResult).getArgs()).containsEntry("val", "123");
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command1);
    }

    /**
     * Asserts that lookup of commands without args works.
     */
    @Test
    void commandArgs() {
        String input = "baa 456";
        LookupResult lookupResult = clingy.parse(input);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult.getPathDangling()).containsExactly("456");
        assertThat(lookupResult.getPathUsed()).containsExactly("baa");
        assertThat(((LookupSuccess) lookupResult).getArgs()).isEmpty();
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command2);
    }

    /**
     * Asserts that lookup of missing commands works.
     */
    @Test
    void commandMissing() {
        @SuppressWarnings("SpellCheckingInspection") String input = "foob";
        LookupResult lookupResult = clingy.parse(input);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_NOT_FOUND);
        assertThat(lookupResult.getPathDangling()).isEmpty();
        assertThat(lookupResult.getPathUsed()).containsExactly(input);
        assertThat(((LookupErrorNotFound) lookupResult).getMissing()).isEqualTo(input);
        assertThat(((LookupErrorNotFound) lookupResult).getSimilar()).contains("foo");
    }

    /**
     * Asserts that lookup of commands with missing args works.
     */
    @Test
    void commandArgsMissing() {
        String input = "foo";
        LookupResult lookupResult = clingy.parse(input);
        assertThat(lookupResult.getType()).isEqualTo(LookupResult.ResultType.ERROR_MISSING_ARGUMENT);
        assertThat(lookupResult.getPathDangling()).isEmpty();
        assertThat(lookupResult.getPathUsed()).containsExactly(input);
        assertThat(((LookupErrorMissingArgs) lookupResult).getMissing()).containsExactly(argument1);
    }
}
