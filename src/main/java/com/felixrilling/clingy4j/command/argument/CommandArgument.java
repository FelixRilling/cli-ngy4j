package com.felixrilling.clingy4j.command.argument;

import com.felixrilling.clingy4j.command.Command;

public class CommandArgument {
    private String name;
    private boolean required;
    private String defaultValue;

    /**
     * @see CommandArgument#CommandArgument(String, boolean, String)
     */
    public CommandArgument(String name, boolean required) {
        this(name, required, null);
    }

    /**
     * Creates a {@link CommandArgument} instance.
     *
     * @param name         Name used for the key of the argument when provided to the {@link Command#fn}.
     * @param required     If the argument is required. If true, an error will be thrown if it is not provided,
     *                     If false, the optional {@link CommandArgument#defaultValue} will be substituted.
     * @param defaultValue The optional value to substitute if the argument is not required and not present.
     */
    public CommandArgument(String name, boolean required, String defaultValue) {
        this.name = name;
        this.required = required;
        if (!required)
            this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
