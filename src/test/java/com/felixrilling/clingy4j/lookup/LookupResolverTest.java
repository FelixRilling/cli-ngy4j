package com.felixrilling.clingy4j.lookup;

import com.felixrilling.clingy4j.Clingy;
import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LookupResolverTest {

    private LookupResolver lookupResolver;

    @Before
    public void before() {
        lookupResolver = new LookupResolver(true);
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} returns null for an empty path.
     */
    @Test
    public void resolveCommandReturnsNullForEmpty() {
        assertThat(lookupResolver.resolve(new CommandMap(), Collections.emptyList())).isNull();
    }

    /**
     * Asserts that {@link LookupResolver#resolve(CommandMap, List)} returns a {@link LookupErrorNotFound} for non-existent commands.
     */
    @Test
    public void resolveCommandReturnsNullForNotFound() {
        assertThat(lookupResolver.resolve(new CommandMap(), Collections.singletonList("foo"))).isInstanceOf(LookupErrorNotFound.class);
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

        ILookupResult lookupResult = lookupResolver.resolve(commandMap, Collections.singletonList(commandName));
        assertThat(lookupResult).isInstanceOf(LookupSuccess.class);
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

        ILookupResult lookupResult = lookupResolver.resolve(commandMap, commandNames);
        assertThat(lookupResult).isInstanceOf(LookupSuccess.class);
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

        ILookupResult lookupResult = lookupResolver.resolve(commandMap1, Arrays.asList(commandName1, commandName2));
        assertThat(lookupResult).isInstanceOf(LookupSuccess.class);
        assertThat(((LookupSuccess) lookupResult).getCommand()).isEqualTo(command1);
    }
}
