package gg.steve.anthem.cmd.misc;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.upgrade.UpgradeType;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhoCmd {

    public static void who(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.who"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.who"));
            return;
        }
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        if (!FactionManager.factionAlreadyExists(args[1])) {
            CommandDebug.FACTION_DOES_NOT_EXIST.message(fPlayer);
            return;
        }
        Faction faction = FactionManager.getFaction(args[1]);
        RelationType type = faction.getRelationManager().getRelationType(fPlayer.getFaction());
        if (type.equals(RelationType.WILDERNESS)) {
            type = RelationType.NEUTRAL;
        }
        MessageType.WHO.message(fPlayer, faction.getName(),
                faction.getFounded(),
                String.valueOf(faction.getRelationManager().getRelationCount(RelationType.ALLY)),
                String.valueOf(RelationType.ALLY.getMaxAmount()),
                faction.getRelationManager().getAlliesAsString(),
                faction.getNumberOnline(),
                faction.getOnlinePlayersAsString(),
                faction.getNumberOffline(),
                faction.getOfflinePlayersAsString(),
                faction.getTotalPlayers(),
                type.getPrefix(),
                type.toString(),
                AnthemFactions.getNumberFormat().format(faction.getWealth()),
                AnthemFactions.getNumberFormat().format(faction.getXp()),
                String.valueOf(faction.getUpgrade(UpgradeType.RAIDING).getLevel()),
                String.valueOf(UpgradeType.RAIDING.getMaxLevel()),
                String.valueOf(faction.getUpgrade(UpgradeType.FARMING).getLevel()),
                String.valueOf(UpgradeType.FARMING.getMaxLevel()),
                String.valueOf(faction.getUpgrade(UpgradeType.WORLD).getLevel()),
                String.valueOf(UpgradeType.WORLD.getMaxLevel()));
    }
}
