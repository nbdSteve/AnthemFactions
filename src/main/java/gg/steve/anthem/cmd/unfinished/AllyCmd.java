package gg.steve.anthem.cmd.unfinished;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.utils.MessageUtil;
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
        if (!fPlayer.hasFactionPermission("factions.player.ally")) {
            MessageUtil.message("lang", "insufficient-role-permission", fPlayer.getPlayer());
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
        if (faction.getRelationManager().isAlly(ally)) {
            MessageUtil.commandDebug(sender, "Error, you are already allies with that faction");
            return;
        }
        if (faction.getRelationManager().hasOutgoingAllyRequest(ally)) {
            MessageUtil.commandDebug(sender, "Error, you have already requested to ally that faction");
            return;
        }
        if (faction.getRelationManager().hasIncomingAllyRequest(ally)) {
            faction.getRelationManager().updateRelation(ally, RelationType.ALLY);
            faction.messageAllOnlinePlayers("lang", "new-ally-alert", "{ally}", ally.getName());
            ally.getRelationManager().updateRelation(faction, RelationType.ALLY);
            ally.messageAllOnlinePlayers("lang", "new-ally-alert", "{ally}", faction.getName());
            return;
        }
        faction.getRelationManager().setAllyRequest(ally);
        faction.messageAllOnlinePlayers("lang", "ally-request-send", "{player}", fPlayer.getPlayer().getName(), "{ally}", ally.getName());
        ally.messageAllOnlinePlayers("lang", "ally-request-receive", "{player}", fPlayer.getPlayer().getName());
    }
}
