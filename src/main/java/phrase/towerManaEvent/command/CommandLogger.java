package phrase.towerManaEvent.command;

import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.command.impl.ManaEventCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandLogger {

    private final Plugin plugin;
    private final Map<String, CommandDescription> commands;

    public CommandLogger(Plugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
        initialize();
    }

    private void initialize() {
        commands.put("event", new CommandDescription("towermanaevent.event", new ManaEventCommand(plugin)));
    }

    public Map<String, CommandDescription> getCommands() {
        return Collections.unmodifiableMap(commands);
    }

}
