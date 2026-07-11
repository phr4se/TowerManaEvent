package phrase.towerManaEvent.command;

import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.command.impl.ManaChancesCommand;
import phrase.towerManaEvent.command.impl.ManaEventCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandLogger {
    private final TowerManaEvent plugin;
    private final Map<String, CommandDescription> commands;

    public CommandLogger(TowerManaEvent plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
        initialize();
    }

    private void initialize() {
        commands.put("event", new CommandDescription("towermanaevent.event", new ManaEventCommand(plugin)));
        commands.put("chances", new CommandDescription("towermanaevent.chances", new ManaChancesCommand(plugin)));
    }

    public Map<String, CommandDescription> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
