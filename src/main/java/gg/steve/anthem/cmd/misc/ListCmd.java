package gg.steve.anthem.cmd.misc;

import gg.steve.anthem.cmd.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCmd {

    public static void list(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can view the faction list");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFactionPermission("factions.player.list")) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, "factions.player.list");
            return;
        }
        MessageUtil.listMessage(fPlayer);
    }
}
