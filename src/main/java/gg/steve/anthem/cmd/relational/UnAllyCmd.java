package gg.steve.anthem.cmd.relational;

import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.exception.CooldownActiveException;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnAllyCmd {

    public static void unAlly(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.UN_ALLY)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.UN_ALLY.get());
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
        if (!faction.getRelationManager().isAlly(ally)) {
            CommandDebug.NOT_ALLIES.message(fPlayer);
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
