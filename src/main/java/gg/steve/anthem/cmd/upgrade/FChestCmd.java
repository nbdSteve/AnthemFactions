package gg.steve.anthem.cmd.upgrade;

import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.upgrade.UpgradeType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FChestCmd {

    public static void chest(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (fPlayer.getFaction().getUpgrade(UpgradeType.RAIDING).getLevel() < 2) {
            MessageType.INSUFFICIENT_UPGRADE_LEVEL.message(fPlayer, UpgradeType.RAIDING.toString(), "2");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.CHEST)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.CHEST.get());
            return;
        }
        fPlayer.getFaction().openfChest(fPlayer);
    }
}
