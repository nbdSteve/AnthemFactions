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

public class EnemyCmd {

    public static void enemy(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can enemy factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you are not in a faction");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.enemy"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.enemy"));
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        if (!FactionManager.factionAlreadyExists(args[1])) {
            MessageUtil.commandDebug(sender, "Error, the faction you tried to enemy does not exist");
            return;
        }
        Faction faction = fPlayer.getFaction();
        Faction enemy = FactionManager.getFaction(args[1]);
        if (faction.getRelationManager().isAlly(enemy)) {
            MessageUtil.commandDebug(sender, "Error, you are must un-ally this faction before you can enemy them");
            return;
        }
        if (faction.getRelationManager().isEnemy(enemy)) {
            MessageUtil.commandDebug(sender, "Error, you are already enemies with that faction");
            return;
        }
        faction.getRelationManager().updateRelation(enemy, RelationType.ENEMY);
        faction.messageAllOnlinePlayers(MessageType.ENEMY_ALERT_SENDER, enemy.getName());
        enemy.messageAllOnlinePlayers(MessageType.ENEMY_ALERT_RECEIVER, faction.getName(), fPlayer.getPlayer().getName());
    }
}
