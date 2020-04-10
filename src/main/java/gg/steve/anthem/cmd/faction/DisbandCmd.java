package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.cmd.MessageType;
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
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can disband factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you are not in a faction");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.disband"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.disband"));
            return;
        }
        if (!CooldownManager.isOnCooldown(fPlayer.getUUID(), CooldownType.DISBAND)) {
            try {
                CooldownManager.addCooldown(fPlayer.getUUID(), new Cooldown(CooldownType.DISBAND));
            } catch (CooldownActiveException e) {
                e.printStackTrace();
            }
            MessageType.DISBAND_CONFIRMATION.message(fPlayer.getPlayer());
            return;
        }
        FactionManager.disbandFaction(FactionManager.getFaction(fPlayer));
    }
}
