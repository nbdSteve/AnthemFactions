package gg.steve.anthem.cmd.upgrade;

import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.upgrade.UpgradeType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FFlyCmd {

    public static void fly(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (fPlayer.getFaction().getUpgrade(UpgradeType.WORLD).getLevel() < 1) {
            MessageType.INSUFFICIENT_UPGRADE_LEVEL.message(fPlayer, UpgradeType.WORLD.toString(), "1");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.FLY)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.FLY.get());
            return;
        }
        if (!fPlayer.isInHomeWorld() || !fPlayer.getFWorld().inFactionLand(fPlayer.getLocation())) {
            MessageType.FLY_DISABLED_REGION.message(fPlayer);
            return;
        }
        if (fPlayer.isBeingRaided()) {
            MessageType.FLY_DISABLED_RAID.message(fPlayer);
            return;
        }
        if (fPlayer.isFlying()) {
            CommandDebug.ALREADY_FLYING.message(fPlayer);
        } else {
            fPlayer.setFlying(true);
            MessageType.FLY_ENABLED.message(fPlayer);
        }
    }
}
