package phrase.towerManaEvent.command;

import phrase.towerManaEvent.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandLogger {

    private final Plugin plugin;
    private final Map<String, CommandDescription> commands;

    public CommandLogger(Plugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
    }

    public void initialize(Plugin plugin) {

    }

    public Map<String, CommandDescription> getCommands() {
        return Collections.unmodifiableMap(commands);
    }

}
