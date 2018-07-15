package com.felixrilling.clingy4j;

import com.felixrilling.clingy4j.command.Command;
import com.felixrilling.clingy4j.command.CommandMap;
import com.felixrilling.clingy4j.command.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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


    /**
     * Resolves a command and its sub-commands.
     *
     * @param path Path to look up.
     * @return Command or null if none is found.
     */
    public ICommand resolveCommand(List<String> path) {
        return resolveCommand(path, new ArrayList<>());

    }

    public ICommand parse(String input) {
        return null;
    }

    /**
     * @see Clingy#resolveCommand(List)
     */
    private ICommand resolveCommand(List<String> path, List<String> pathUsed) {
        if (path.isEmpty())
            return null;

        String current = path.get(0);

        if (!hasCommand(current))
            return null;


        ICommand command = getCommand(current);

        if (path.size() == 1)
            return command;

        if (command.getSub() != null) {
            path.remove(0);
            pathUsed.add(0, current);
            return command.getSub().resolveCommand(path, pathUsed);
        } else {
            //TODO return pathUsed as well
            return command;
        }
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
