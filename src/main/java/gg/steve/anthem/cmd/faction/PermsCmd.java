package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermsCmd {

    public static void perms(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.PERMS)) {
            MessageType.PERMISSION_DEBUG.message(fPlayer, PermissionNode.PERMS.get());
            return;
        }
        FactionManager.getPermissionGui().setFaction(fPlayer.getFaction());
        FactionManager.getPermissionGui().open(fPlayer.getPlayer());
    }
}
