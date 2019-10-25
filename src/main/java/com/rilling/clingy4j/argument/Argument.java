package com.rilling.clingy4j.argument;

import com.rilling.clingy4j.command.Command;

/**
 * Argument to be used when creating {@link Command}s.
 */
public class Argument {

    private final String name;
    private final boolean required;
    private final String defaultValue;

    /**
     * @see Argument#Argument(String, boolean, String)
     */
    public Argument(String name, boolean required) {
        this(name, required, null);
    }

    /**
     * Creates a {@link Argument} instance.
     *
     * @param name         Name used for the key of the argument when provided to the {@link Command} function.
     * @param required     If the argument is required. If true, an error will be thrown if it is not provided,
     *                     If false, the optional {@link Argument#defaultValue} will be substituted.
     * @param defaultValue The optional value to substitute if the argument is not required and not present.
     */
    public Argument(String name, boolean required, String defaultValue) {
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("WeakerAccess")
    public String getName() {
        return name;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isRequired() {
        return required;
    }

    @SuppressWarnings("WeakerAccess")
    public String getDefaultValue() {
        return defaultValue;
    }

}
