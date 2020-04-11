package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCmd {

    public static void leave(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.leave"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.leave"));
            return;
        }
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (fPlayer.getRole().equals(Role.OWNER)) {
            CommandDebug.OWNER_LEAVE.message(fPlayer);
            return;
        }
        fPlayer.getFaction().removePlayer(fPlayer.getUUID());
        FPlayerManager.updateFPlayer(fPlayer.getUUID());
        MessageType.LEAVE.message(fPlayer, fPlayer.getFaction().getName());
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.LEAVE_ALERT, fPlayer.getPlayer().getName());
    }
}