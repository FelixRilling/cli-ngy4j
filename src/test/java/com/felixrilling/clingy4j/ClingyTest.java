package com.felixrilling.clingy4j;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClingyTest {

    @Test
    public void ClingyConstructsWithoutCommands() {
        Clingy clingy = new Clingy();

        assertThat(clingy.getMap()).isEmpty();
        assertThat(clingy.getMapAliased()).isEmpty();
    }

    @Test
    public void ClingyConstructsWithCommands() {
        CommandMap commandMap = new CommandMap();
        Command command = new Command(null, null, null, null, null);
        commandMap.put("foo", command);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.getMap()).hasSize(1).containsValue(command);
        assertThat(clingy.getMapAliased()).hasSize(1).containsValue(command);
    }

    @Test
    public void ClingyUpdatesAliasedMap() {
        CommandMap commandMap = new CommandMap();
        List<String> aliases = Arrays.asList("bar", "fizz");
        String commandName = "foo";
        Command command = new Command(null, aliases, null, null, null);
        commandMap.put(commandName, command);
        Clingy clingy = new Clingy(commandMap);

        assertThat(clingy.getMap()).hasSize(1).containsValue(command);
        assertThat(clingy.getMapAliased()).containsKeys(commandName, aliases.get(0), aliases.get(1));
    }

}