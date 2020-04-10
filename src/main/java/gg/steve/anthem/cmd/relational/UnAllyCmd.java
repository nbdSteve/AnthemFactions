package gg.steve.anthem.cmd.relational;

import gg.steve.anthem.cmd.MessageType;
import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.exception.CooldownActiveException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnAllyCmd {

    public static void unAlly(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can un-ally factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you are not in a faction");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.un-ally"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.un-ally"));
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        if (!FactionManager.factionAlreadyExists(args[1])) {
            MessageUtil.commandDebug(sender, "Error, the faction you tried to un-ally does not exist");
            return;
        }
        Faction faction = fPlayer.getFaction();
        Faction ally = FactionManager.getFaction(args[1]);
        if (!faction.getRelationManager().isAlly(ally)) {
            MessageUtil.commandDebug(sender, "Error, you are not allies with that faction");
            return;
        }
        if (!CooldownManager.isOnCooldown(fPlayer.getUUID(), CooldownType.UN_ALLY)) {
            try {
                CooldownManager.addCooldown(fPlayer.getUUID(), new Cooldown(CooldownType.UN_ALLY));
            } catch (CooldownActiveException e) {
                e.printStackTrace();
            }
            MessageType.UN_ALLY_CONFIRMATION.message(fPlayer, ally.getName());
            return;
        }
        faction.getRelationManager().updateRelation(ally, RelationType.NEUTRAL);
        faction.messageAllOnlinePlayers(MessageType.UN_ALLY_ALERT_SENDER, ally.getName());
        ally.getRelationManager().updateRelation(faction, RelationType.NEUTRAL);
        ally.messageAllOnlinePlayers(MessageType.UN_ALLY_ALERT_RECEIVER, faction.getName(), fPlayer.getPlayer().getName());
    }
}
