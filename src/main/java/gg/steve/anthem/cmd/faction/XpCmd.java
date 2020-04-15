package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XpCmd {

    public static void xp(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.XP_QUERY)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.XP_QUERY.get());
            return;
        }
        MessageType.XP_QUERY.message(fPlayer, AnthemFactions.getNumberFormat().format(fPlayer.getFaction().getXp()));
    }
}
