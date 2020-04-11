package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromoteCmd {

    public static void promote(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
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
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.promote"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.promote"));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageUtil.commandDebug(sender, "Error, the player you are promoting must be online");
            return;
        }
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (target.getUniqueId().equals(player.getUniqueId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot promote yourself");
            return;
        }
        if (!fPlayer.getFaction().equals(tPlayer.getFaction())) {
            MessageUtil.commandDebug(sender, "Error, you cannot promote someone who is not in your faction");
            return;
        }
        if (Role.higherRole(tPlayer.getRole(), fPlayer.getRole())) {
            MessageUtil.commandDebug(sender, "Error, you cannot promote someone who is a higher, or the same rank as you");
            return;
        }
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.PROMOTION, player.getName(), target.getName(), Role.getRoleByWeight(tPlayer.getRole().getWeight() + 1).toString());
        fPlayer.getFaction().promote(tPlayer.getUUID());
        FPlayerManager.updateFPlayer(tPlayer.getUUID());
    }
}
