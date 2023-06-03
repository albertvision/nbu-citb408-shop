package me.yasen.models;

public class Command {
    private final String key;
    private final Class<? extends me.yasen.commands.Command> className;
    public Command(String key, Class<? extends me.yasen.commands.Command> className) {
        this.key = key;
        this.className = className;
    }

    public Class<? extends me.yasen.commands.Command> getClassName() {
        return className;
    }

    public String getKey() {
        return key;
    }


    @Override
    public String toString() {
        return getKey();
    }
}
