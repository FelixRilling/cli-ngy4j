package com.felixrilling.clingy4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Clingy {
    private CommandMap map;
    private CommandMap mapAliased;

    private Logger logger = LoggerFactory.getLogger(Clingy.class);


    public Clingy() {
        this(new CommandMap());
    }

    public Clingy(CommandMap commands) {
        this.map = commands;
        updateAliasedMap();
    }

    public CommandMap getMap() {
        return map;
    }

    public CommandMap getMapAliased() {
        return mapAliased;
    }


    public void setCommand(String key, ICommand command) {
        map.put(key, command);
        updateAliasedMap();
    }

    public ICommand getCommand(String key) {
        return mapAliased.get(key);
    }

    public boolean hasCommand(String key) {
        return mapAliased.containsKey(key);
    }


    public ICommand resolveCommand(String[] path) {
        return null;
    }

    public ICommand parse(String input) {
        return null;
    }


    private void updateAliasedMap() {
        CommandMap result = new CommandMap(map);

        for (Map.Entry<String, ICommand> entry : map.entrySet()) {
            ICommand value = entry.getValue();
            List<String> aliases = value.getAlias();

            if (aliases != null) {
                aliases.forEach(alias -> {
                    if (result.containsKey(alias)) {
                        logger.warn("Alias {} conflicts with a previously defined key, will be ignored", alias);
                    } else {
                        result.put(alias, value);
                    }
                });
            }
        }

        mapAliased = result;
    }
}
