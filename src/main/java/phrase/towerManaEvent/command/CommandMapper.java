package phrase.towerManaEvent.command;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.config.data.Messages;

public class CommandMapper {
    private final CommandLogger commandLogger;
    private final Plugin plugin;

    public CommandMapper(CommandLogger commandLogger, Plugin plugin) {
        this.commandLogger = commandLogger;
        this.plugin = plugin;
    }

    public CommandResult mapCommand(Player player, String label, String[] args) {
        Messages messages = plugin.getConfigFile().getMessages();
        CommandDescription commandDescription = commandLogger.getCommands().get(label);
        if (commandDescription == null)
            return new CommandResult(messages.unknownCommand(), CommandResult.ResultStatus.UNKNOWN_COMMAND);
        if (!player.hasPermission(commandDescription.getPermission()))
            return new CommandResult(messages.noPermission(), CommandResult.ResultStatus.NO_PERMISSION);
        CommandHandler commandHandler = commandDescription.getCommandHandler();
        if (commandHandler == null) return new CommandResult(messages.error(), CommandResult.ResultStatus.ERROR);
        boolean success = commandHandler.handler(player, args);
        if (!success)
            return new CommandResult(messages.incorrectArguments(), CommandResult.ResultStatus.INCORRECT_ARGUMENTS);
        return new CommandResult(null, CommandResult.ResultStatus.SUCCESS);
    }
}
