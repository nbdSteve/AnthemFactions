package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaveCmd {

    public static void leave(CommandSender sender) {
//        if (!PermissionQueryUtil.hasPermission(sender, "player.leave")) {
//            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.leave"));
//            return;
//        }
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can leave factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFactionPermission("factions.player.leave")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.leave"));
            return;
        }
        if (!fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you are not a member of a faction");
            return;
        }
        if (fPlayer.getRole().equals(Role.OWNER)) {
            MessageUtil.commandDebug(sender, "Error, you cannot leave the faction since you are the owner - try /f disband");
            return;
        }
        fPlayer.getFaction().removePlayer(fPlayer.getUUID());
        FPlayerManager.updateFPlayer(fPlayer.getUUID());
        MessageUtil.message("lang", "leave", fPlayer.getPlayer(), "{faction-name}", fPlayer.getFaction().getName());
        for (UUID uuid : fPlayer.getFaction().getPlayers()) {
            Player member = Bukkit.getPlayer(uuid);
            MessageUtil.message("lang", "leave-alert", member, "{leaver}", fPlayer.getPlayer().getName());
        }
    }
}
