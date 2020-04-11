package gg.steve.anthem.cmd.relational;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NeutralCmd {

    public static void neutral(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.neutral"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.neutral"));
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
        Faction faction = fPlayer.getFaction();
        Faction neutral = FactionManager.getFaction(args[1]);
        if (faction.getId().equals(neutral.getId())) {
            CommandDebug.RELATION_TARGET_CAN_NOT_BE_SELF.message(fPlayer);
            return;
        }
        if (neutral.getId().equals(FactionManager.getWildernessId())) {
            CommandDebug.RELATION_TARGET_WILDERNESS.message(fPlayer);
            return;
        }
        if (faction.getRelationManager().isAlly(neutral)) {
            CommandDebug.UN_ALLY_REQUIRED.message(fPlayer);
            return;
        }
        if (faction.getRelationManager().isNeutral(neutral)) {
            CommandDebug.RELATION_ALREADY_SET.message(fPlayer);
            return;
        }
        faction.getRelationManager().updateRelation(neutral, RelationType.NEUTRAL);
        faction.messageAllOnlinePlayers(MessageType.NEUTRAL_ALERT_SENDER, neutral.getName());
        neutral.messageAllOnlinePlayers(MessageType.NEUTRAL_ALERT_RECEIVER, faction.getName(), fPlayer.getPlayer().getName());
    }
}
