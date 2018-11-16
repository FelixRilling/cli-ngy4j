# cli-ngy4j

> A module for cli-input text parsing with support for nested commands and did-you-mean feature.

## Introduction

Java counterpart of [cli-ngy](https://github.com/FelixRilling/cli-ngy).

## Usage

```java
package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.argument.Argument;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.lookup.LookupErrorMissingArgs;
import com.felixrilling.clingy4j.lookup.LookupErrorNotFound;
import com.felixrilling.clingy4j.lookup.LookupResult;
import com.felixrilling.clingy4j.lookup.LookupSuccess;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ClingyIT {

    @Test
    public void runClingy() {
        Map<String, Command> commandMap = new HashMap<>();

        Argument argument1 = new Argument("val", true);
        Command command1 = new Command(
            System.out::println, // Function
            Arrays.asList("fizz", "fuu"), // Aliases
            Collections.singletonList(argument1) // Arguments
        );
        commandMap.put("foo", command1);

        Command command2 = new Command(
            System.out::println,
            Collections.singletonList("baa"),
            Collections.emptyList()
        );
        commandMap.put("bar", command2);

        Clingy clingy = new Clingy(commandMap);

        String input1 = "foo 123";
        LookupResult lookupResult1 = clingy.parse(input1);
        assertThat(lookupResult1.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult1.getPathDangling()).containsExactly("123");
        assertThat(lookupResult1.getPath()).containsExactly("foo");
        assertThat(((LookupSuccess) lookupResult1).getArgs()).containsEntry("val", "123");
        assertThat(((LookupSuccess) lookupResult1).getCommand()).isEqualTo(command1);

        String input2 = "baa 456";
        LookupResult lookupResult2 = clingy.parse(input2);
        assertThat(lookupResult2.getType()).isEqualTo(LookupResult.ResultType.SUCCESS);
        assertThat(lookupResult2.getPathDangling()).containsExactly("456");
        assertThat(lookupResult2.getPath()).containsExactly("baa");
        assertThat(((LookupSuccess) lookupResult2).getArgs()).isEmpty();
        assertThat(((LookupSuccess) lookupResult2).getCommand()).isEqualTo(command2);

        String input3 = "foob";
        LookupResult lookupResult3 = clingy.parse(input3);
        assertThat(lookupResult3.getType()).isEqualTo(LookupResult.ResultType.ERROR_NOT_FOUND);
        assertThat(lookupResult3.getPathDangling()).isEmpty();
        assertThat(lookupResult3.getPath()).containsExactly(input3);
        assertThat(((LookupErrorNotFound) lookupResult3).getMissing()).isEqualTo(input3);
        assertThat(((LookupErrorNotFound) lookupResult3).getSimilar()).contains("foo");

        String input4 = "foo";
        LookupResult lookupResult4 = clingy.parse(input4);
        assertThat(lookupResult4.getType()).isEqualTo(LookupResult.ResultType.ERROR_MISSING_ARGUMENT);
        assertThat(lookupResult4.getPathDangling()).isEmpty();
        assertThat(lookupResult4.getPath()).containsExactly(input4);
        assertThat(((LookupErrorMissingArgs) lookupResult4).getMissing()).containsExactly(argument1);
    }
}
```
