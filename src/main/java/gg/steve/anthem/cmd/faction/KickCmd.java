package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCmd {

    public static void kick(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can kick others from the factions");
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        Player player = (Player) sender;
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you must create a faction using /f create {name} first");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.kick"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.kick"));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageUtil.commandDebug(sender, "Error, the player you are kicking must be online");
            return;
        }
        Faction faction = fPlayer.getFaction();
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (target.getUniqueId().equals(player.getUniqueId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot kick yourself");
            return;
        }
        if (!faction.isMember(tPlayer)) {
            MessageUtil.commandDebug(sender, "Error, the player you are trying to kick is not a member of your faction");
            return;
        }
        if (!Role.higherRole(fPlayer.getRole(), tPlayer.getRole())) {
            MessageUtil.commandDebug(sender, "Error, you cannot kick a player who is a higher role than you");
            return;
        }
        faction.removePlayer(tPlayer.getUUID());
        FPlayerManager.updateFPlayer(tPlayer.getUUID());
        MessageType.KICK_RECEIVER.message(target, fPlayer.getFaction().getName(), player.getName());
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.KICK_KICKER, player.getName(), target.getName());
    }
}
