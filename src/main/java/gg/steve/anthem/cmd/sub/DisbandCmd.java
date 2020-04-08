package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.exception.CooldownActiveException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandCmd {

    public static void disband(CommandSender sender) {
//        if (!PermissionQueryUtil.hasPermission(sender, "player.disband")) {
//            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.disband"));
//            return;
//        }
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can disband factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you are not in a faction");
            return;
        }
        if (!fPlayer.hasFactionPermission("factions.player.disband")) {
            MessageUtil.message("lang", "insufficient-role-permission", fPlayer.getPlayer());
            return;
        }
        if (!CooldownManager.isOnCooldown(fPlayer.getUUID(), CooldownType.DISBAND)) {
            try {
                CooldownManager.addCooldown(fPlayer.getUUID(), new Cooldown(CooldownType.DISBAND));
            } catch (CooldownActiveException e) {
                e.printStackTrace();
            }
            MessageUtil.message("lang", "disband-confirmation", fPlayer.getPlayer());
            return;
        }
        FactionManager.disbandFaction(FactionManager.getFaction(fPlayer));
    }
}
