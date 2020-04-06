package gg.steve.anthem.cmd;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("f")) {
            if (args.length == 0) {
                MessageUtil.helpMessage(sender);
            } else if (args.length == 2) {
                if (!(sender instanceof Player)) {
                    MessageUtil.commandDebug(sender, "Error, only players can create factions.");
                }
                switch (args[0].toLowerCase()) {
                    case "create":
                        FactionManager.createFaction(args[1], (Player) sender);
                        break;
                    case "home":
                        break;
                    case "raid":
                        break;
                    case "upgrade":
                        break;
                    case "perms":
                        break;
                    case "disband":
                        FactionManager.disbandFaction(args[1].toLowerCase());
                        break;
                    case "promote":
                        break;
                    case "demote":
                        break;
                    case "ally":
                        break;
                    case "who":
                        break;
                    case "invite":
                        break;
                    default:
                        MessageUtil.commandDebug(sender, "Invalid initial argument, please check /f help");
                }
            } else {
                MessageUtil.commandDebug(sender, "Invalid arguments, please check /f help");
            }
        }
        return true;
    }
}
