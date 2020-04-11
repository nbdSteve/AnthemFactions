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

public class AllyCmd {

    public static void ally(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.ally"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.ally"));
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
        Faction ally = FactionManager.getFaction(args[1]);
        if (faction.getId().equals(ally.getId())) {
            CommandDebug.RELATION_TARGET_CAN_NOT_BE_SELF.message(fPlayer);
            return;
        }
        if (ally.getId().equals(FactionManager.getWildernessId())) {
            CommandDebug.RELATION_TARGET_WILDERNESS.message(fPlayer);
            return;
        }
        if (faction.getRelationManager().isAlly(ally)) {
            CommandDebug.RELATION_ALREADY_SET.message(fPlayer);
            return;
        }
        if (faction.getRelationManager().hasOutgoingAllyRequest(ally)) {
            CommandDebug.PENDING_ALLY_REQUEST.message(fPlayer);
            return;
        }
        if (faction.getRelationManager().hasIncomingAllyRequest(ally)) {
            if (faction.getRelationManager().getRelationCount(RelationType.ALLY) == RelationType.ALLY.getMaxAmount()
                    || ally.getRelationManager().getRelationCount(RelationType.ALLY) == RelationType.ALLY.getMaxAmount()) {
                faction.messageAllOnlinePlayers(MessageType.ALLY_DECLINED, ally.getName());
                ally.messageAllOnlinePlayers(MessageType.ALLY_DECLINED, faction.getName());
                return;
            }
            faction.getRelationManager().updateRelation(ally, RelationType.ALLY);
            faction.messageAllOnlinePlayers(MessageType.ALLY_ALERT, ally.getName());
            ally.getRelationManager().updateRelation(faction, RelationType.ALLY);
            ally.messageAllOnlinePlayers(MessageType.ALLY_ALERT, faction.getName());
            return;
        }
        if (faction.getRelationManager().getRelationCount(RelationType.ALLY) == RelationType.ALLY.getMaxAmount()) {
            faction.messageAllOnlinePlayers(MessageType.ALLY_DECLINED, ally.getName());
            return;
        }
        faction.getRelationManager().setAllyRequest(ally);
        faction.messageAllOnlinePlayers(MessageType.ALLY_REQUEST_SEND, ally.getName(), fPlayer.getPlayer().getName());
        ally.messageAllOnlinePlayers(MessageType.ALLY_REQUEST_RECEIVE, faction.getName(), fPlayer.getPlayer().getName());
    }
}
