package gg.steve.anthem.cmd.relational;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnemyCmd {

    public static void enemy(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.ENEMY)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.ENEMY.get());
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
        Faction enemy = FactionManager.getFaction(args[1]);
        if (faction.getId().equals(enemy.getId())) {
            CommandDebug.RELATION_TARGET_CAN_NOT_BE_SELF.message(fPlayer);
            return;
        }
        if (enemy.getId().equals(FactionManager.getWildernessId())) {
            CommandDebug.RELATION_TARGET_WILDERNESS.message(fPlayer);
            return;
        }
        if (faction.getRelationManager().isAlly(enemy)) {
            CommandDebug.UN_ALLY_REQUIRED.message(fPlayer);
            return;
        }
        if (faction.getRelationManager().isEnemy(enemy)) {
            CommandDebug.RELATION_ALREADY_SET.message(fPlayer);
            return;
        }
        faction.getRelationManager().updateRelation(enemy, RelationType.ENEMY);
        faction.messageAllOnlinePlayers(MessageType.ENEMY_ALERT_SENDER, enemy.getName());
        enemy.messageAllOnlinePlayers(MessageType.ENEMY_ALERT_RECEIVER, faction.getName(), fPlayer.getPlayer().getName());
    }
}
