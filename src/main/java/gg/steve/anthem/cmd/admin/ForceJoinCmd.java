package gg.steve.anthem.cmd.admin;

import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.cooldown.exception.NotOnCooldownException;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceJoinCmd {

    public static void join(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        if (!PermissionNode.FORCE_JOIN.hasPermission(sender)) {
            MessageType.PERMISSION_DEBUG.message(sender, PermissionNode.FORCE_JOIN.get());
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(sender);
            return;
        }
        if (!FactionManager.factionAlreadyExists(args[1])) {
            CommandDebug.FACTION_DOES_NOT_EXIST.message(sender);
            return;
        }
        Faction faction = FactionManager.getFaction(args[1]);
        faction.messageAllOnlinePlayers(MessageType.MEMBER_JOIN_ALERT, fPlayer.getName());
        faction.addPlayer(fPlayer.getUUID(), Role.MEMBER);
        FPlayerManager.updateFPlayer(fPlayer.getUUID());
        MessageType.JOIN.message(fPlayer, faction.getName());
    }
}
