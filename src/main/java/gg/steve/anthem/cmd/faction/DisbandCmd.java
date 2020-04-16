package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.cooldown.exception.CooldownActiveException;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandCmd {

    public static void disband(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.DISBAND)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.DISBAND.get());
            return;
        }
        if (fPlayer.isBeingRaided() || fPlayer.isRaiding()) {
            CommandDebug.DISBAND_DURING_RAID.message(fPlayer);
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
