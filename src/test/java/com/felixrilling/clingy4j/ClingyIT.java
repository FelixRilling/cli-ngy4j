package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.result.LookupErrorMissingArgs;
import com.felixrilling.clingy4j.lookup.result.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.result.LookupResult;
import com.felixrilling.clingy4j.lookup.result.LookupSuccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

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
    public void setup() {
        CommandMap commandMap = new CommandMap();

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
     * Asserts that lookup of commands works.
     */
    @Test
    public void commandNormal() {
        String input1 = "foo 123";
        LookupResult lookupResult1 = clingy.parse(input1);
        assertThat(lookupResult1.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult1.getPathDangling()).containsExactly("123");
        assertThat(lookupResult1.getPathUsed()).containsExactly("foo");
        assertThat(((LookupSuccess) lookupResult1).getArgs()).containsEntry("val", "123");
        assertThat(((LookupSuccess) lookupResult1).getCommand()).isEqualTo(command1);
    }

    /**
     * Asserts that lookup of commands with args works.
     */
    @Test
    public void commandArgs() {
        String input2 = "baa 456";
        LookupResult lookupResult2 = clingy.parse(input2);
        assertThat(lookupResult2.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult2.getPathDangling()).containsExactly("456");
        assertThat(lookupResult2.getPathUsed()).containsExactly("baa");
        assertThat(((LookupSuccess) lookupResult2).getArgs()).isEmpty();
        assertThat(((LookupSuccess) lookupResult2).getCommand()).isEqualTo(command2);
    }

    /**
     * Asserts that lookup of missing commands works.
     */
    @Test
    public void commandMissing() {
        String input3 = "foob";
        LookupResult lookupResult3 = clingy.parse(input3);
        assertThat(lookupResult3.getType()).isEqualTo(LookupResult.ResultType.ERROR_NOT_FOUND);
        assertThat(lookupResult3.getPathDangling()).isEmpty();
        assertThat(lookupResult3.getPathUsed()).containsExactly(input3);
        assertThat(((LookupErrorNotFound) lookupResult3).getMissing()).isEqualTo(input3);
        assertThat(((LookupErrorNotFound) lookupResult3).getSimilar()).contains("foo");
    }

    /**
     * Asserts that lookup of commands with missing args works.
     */
    @Test
    public void commandArgsMissing() {
        String input4 = "foo";
        LookupResult lookupResult4 = clingy.parse(input4);
        assertThat(lookupResult4.getType()).isEqualTo(LookupResult.ResultType.ERROR_MISSING_ARGUMENT);
        assertThat(lookupResult4.getPathDangling()).isEmpty();
        assertThat(lookupResult4.getPathUsed()).containsExactly(input4);
        assertThat(((LookupErrorMissingArgs) lookupResult4).getMissing()).containsExactly(argument1);
    }
}
