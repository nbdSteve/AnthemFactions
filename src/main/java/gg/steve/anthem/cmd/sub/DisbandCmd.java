package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandCmd {

    public static void disband(CommandSender sender) {
        if (!PermissionQueryUtil.hasPermission(sender, "player.disband")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.disband"));
            return;
        }
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can disband factions");
            return;
        }
        Player player = (Player) sender;
        FactionManager.disbandFaction(FactionManager.getFaction(FPlayerManager.getFPlayer(player.getUniqueId())));
    }
}
