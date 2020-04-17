package gg.steve.anthem.cmd.admin;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BypassCmd {

    public static void bypass(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        if (!PermissionNode.BYPASS.hasPermission(sender)) {
            MessageType.PERMISSION_DEBUG.message(sender, PermissionNode.BYPASS.get());
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (fPlayer.isBypassed()) {
            fPlayer.setBypassed(false);
            MessageType.DISABLE_BYPASS.message(fPlayer);
        } else {
            fPlayer.setBypassed(true);
            MessageType.ENABLE_BYPASS.message(fPlayer);
        }
    }
}
