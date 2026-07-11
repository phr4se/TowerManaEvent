package phrase.towerManaEvent.command.impl;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.command.CommandHandler;
import phrase.towerManaEvent.config.data.CommandMessages;
import phrase.towerManaEvent.event.EventManager;
import phrase.towerManaEvent.event.exception.EventAlreadyRun;
import phrase.towerManaEvent.event.exception.SchematicDamaged;
import phrase.towerManaEvent.event.exception.SchematicNotExist;
import phrase.towerManaEvent.util.Utils;

public class ManaEventCommand implements CommandHandler {
    private final TowerManaEvent plugin;

    public ManaEventCommand(TowerManaEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean handler(Player player, String[] args) {
        if (args.length < 2) return false;
        CommandMessages commandMessages = plugin.getConfigFile().getCommandMessages();
        EventManager eventManager = plugin.getEventManager();
        try {
            eventManager.startEvent();
            Utils.sendMessage(player, commandMessages.eventRun());
        } catch (EventAlreadyRun e) {
            Utils.sendMessage(player, commandMessages.eventAlreadyRun());
        } catch (SchematicDamaged e) {
            Utils.sendMessage(player, commandMessages.schematicDamaged());
        } catch (SchematicNotExist e) {
            Utils.sendMessage(player, commandMessages.schematicNotExist());
        }
        return true;
    }
}
