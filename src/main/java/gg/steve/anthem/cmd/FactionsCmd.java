package gg.steve.anthem.cmd;

import com.sun.prism.shader.FillPgram_LinearGradient_PAD_AlphaTest_Loader;
import gg.steve.anthem.cmd.sub.HomeCmd;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.Bukkit;
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
            } else if (args.length > 0) {
                if (!(sender instanceof Player)) {
                    MessageUtil.commandDebug(sender, "Error, only players can create factions.");
                    return true;
                }
                Player player = (Player) sender;
                FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
                switch (args[0].toLowerCase()) {
                    case "create":
                        FactionManager.createFaction(args[1], player);
                        break;
                    case "home":
                        HomeCmd.teleportHome(fPlayer);
                        break;
                    case "sethome":
                        HomeCmd.setHome(fPlayer);
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
