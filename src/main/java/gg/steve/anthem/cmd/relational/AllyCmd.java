package gg.steve.anthem.cmd.relational;

import gg.steve.anthem.cmd.MessageType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllyCmd {

    public static void ally(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can ally factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you are not in a faction");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.ally"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.ally"));
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        if (!FactionManager.factionAlreadyExists(args[1])) {
            MessageUtil.commandDebug(sender, "Error, the faction you tried to ally does not exist");
            return;
        }
        Faction faction = fPlayer.getFaction();
        Faction ally = FactionManager.getFaction(args[1]);
        if (faction.getId().equals(ally.getId())) {
            // cannot ally self
            return;
        }
        if (faction.getRelationManager().isAlly(ally)) {
            MessageUtil.commandDebug(sender, "Error, you are already allies with that faction");
            return;
        }
        if (faction.getRelationManager().hasOutgoingAllyRequest(ally)) {
            MessageUtil.commandDebug(sender, "Error, you have already requested to ally that faction");
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
