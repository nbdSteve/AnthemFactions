package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickCmd {

    public static void kick(CommandSender sender, String[] args) {
        if (!PermissionQueryUtil.hasPermission(sender, "player.kick")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.kick"));
            return;
        }
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can kick others from the factions");
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[1]);
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        Faction faction = fPlayer.getFaction();
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you must create a faction using /f create {name} first");
            return;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot kick yourself");
            return;
        }
        if (fPlayer.getRole().equals(Role.MEMBER)) {
            MessageUtil.message("lang", "insufficient-role-permission", player);
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
        MessageUtil.message("lang", "faction-kick-receiver", target, "{kicker}", player.getName(), "{faction-name}", fPlayer.getFaction().getName());
        for (UUID uuid : fPlayer.getFaction().getPlayers()) {
            Player member = Bukkit.getPlayer(uuid);
            MessageUtil.message("lang", "faction-kick-kicker", member, "{kicker}", player.getName(), "{kicked}", target.getName());
        }
    }
}
