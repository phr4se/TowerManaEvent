package phrase.towerManaEvent.command;

import org.bukkit.entity.Player;

public class CommandMapper {

    private final CommandLogger commandLogger;

    public CommandMapper(CommandLogger commandLogger) {
        this.commandLogger = commandLogger;
    }

    public CommandResult mapCommand(Player player, String label, String[] args) {

        CommandDescription commandDescription = commandLogger.getCommands().get(label);

        if(commandDescription == null) return new CommandResult("", CommandResult.ResultStatus.UNKNOWN_COMMAND);

        if(!player.hasPermission(commandDescription.getPermission())) return new CommandResult("", CommandResult.ResultStatus.NO_PERMISSION);

        CommandHandler commandHandler = commandDescription.getCommandHandler();

        if(commandHandler == null) return new CommandResult("", CommandResult.ResultStatus.ERROR);

        return new CommandResult(null, CommandResult.ResultStatus.SUCCESS);

    }

}
