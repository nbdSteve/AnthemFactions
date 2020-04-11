package gg.steve.anthem.cmd.relational;

import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NeutralCmd {

    public static void neutral(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can neutral factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you are not in a faction");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.neutral"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.neutral"));
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        if (!FactionManager.factionAlreadyExists(args[1])) {
            MessageUtil.commandDebug(sender, "Error, the faction you tried to neutral does not exist");
            return;
        }
        Faction faction = fPlayer.getFaction();
        Faction neutral = FactionManager.getFaction(args[1]);
        if (faction.getId().equals(neutral.getId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot neutral yourself");
            return;
        }
        if (neutral.getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot neutral wilderness");
            return;
        }
        if (faction.getRelationManager().isAlly(neutral)) {
            MessageUtil.commandDebug(sender, "Error, you are must un-ally this faction in order to neutral them");
            return;
        }
        if (faction.getRelationManager().isNeutral(neutral)) {
            MessageUtil.commandDebug(sender, "Error, you are already neutral with that faction");
            return;
        }
        faction.getRelationManager().updateRelation(neutral, RelationType.NEUTRAL);
        faction.messageAllOnlinePlayers(MessageType.NEUTRAL_ALERT_SENDER, neutral.getName());
        neutral.messageAllOnlinePlayers(MessageType.NEUTRAL_ALERT_RECEIVER, faction.getName(), fPlayer.getPlayer().getName());
    }
}
