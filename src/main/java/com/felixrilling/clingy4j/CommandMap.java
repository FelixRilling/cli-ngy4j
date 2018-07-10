package com.felixrilling.clingy4j;

import java.util.HashMap;

public class CommandMap extends HashMap<String, ICommand> {
    public CommandMap() {
        super();
    }

    public CommandMap(CommandMap map) {
        super(map);
    }
}
