package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Clingy {
    private CommandMap map;
    private CommandMap mapAliased;

    private Logger logger = LoggerFactory.getLogger(Clingy.class);

    /**
     * Creates a {@link Clingy} instance.
     */
    public Clingy() {
        this(new CommandMap());
    }

    /**
     * Creates a {@link Clingy} instance.
     *
     * @param commands Map of {@link Command}s to init the instance with.
     */
    public Clingy(CommandMap commands) {
        this.map = commands;
        updateAliasedMap();
    }

    /**
     * Sets a new {@link Command} to the {@link Clingy} instance.
     *
     * @param key     Key of the new {@link Command}.
     * @param command {@link Command} to set.
     */
    public void setCommand(String key, ICommand command) {
        map.put(key, command);
        updateAliasedMap();
    }

    /**
     * Gets a {@link Command} from the {@link Clingy} instance.
     */
    public ICommand getCommand(String key) {
        return mapAliased.get(key);
    }

    /**
     * Checks if a {@link Command} exists in the {@link Clingy} instance.
     */
    public boolean hasCommand(String key) {
        return mapAliased.containsKey(key);
    }


    public ICommand resolveCommand(String[] path) {
        return null;
    }

    public ICommand parse(String input) {
        return null;
    }

    /**
     * Updates the internal aliased Map to contain the aliased keys.
     */
    private void updateAliasedMap() {
        CommandMap result = new CommandMap(map);

        for (Map.Entry<String, ICommand> entry : map.entrySet()) {
            for (String alias : entry.getValue().getAlias()) {
                if (result.containsKey(alias)) {
                    logger.warn("Alias '{}' conflicts with a previously defined key, will be ignored.", alias);
                } else {
                    result.put(alias, entry.getValue());
                }
            }
        }

        mapAliased = result;
    }
}
