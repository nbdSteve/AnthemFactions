package gg.steve.anthem.cmd.unfinished;

import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.upgrade.UpgradeType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaidCmd {

    public static void raid(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.RAID)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.RAID.get());
            return;
        }
        if (fPlayer.getFaction().getUpgrade(UpgradeType.RAIDING).getLevel() < 1) {
            MessageType.INSUFFICIENT_UPGRADE_LEVEL.message(fPlayer, UpgradeType.RAIDING.toString(), "1");
            return;
        }
    }
}
