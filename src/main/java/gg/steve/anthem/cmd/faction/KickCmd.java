package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCmd {

    public static void kick(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.KICK)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.KICK.get());
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CommandDebug.TARGET_NOT_ONLINE.message(fPlayer);
            return;
        }
        Faction faction = fPlayer.getFaction();
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (target.getUniqueId().equals(fPlayer.getUUID())) {
            CommandDebug.TARGET_CAN_NOT_BE_SELF.message(fPlayer);
            return;
        }
        if (!faction.isMember(tPlayer)) {
            CommandDebug.TARGET_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!Role.higherRole(fPlayer.getRole(), tPlayer.getRole())) {
            CommandDebug.KICK_SAME_OR_HIGHER_RANK.message(fPlayer);
            return;
        }
        faction.removePlayer(tPlayer.getUUID());
        FPlayerManager.updateFPlayer(tPlayer.getUUID());
        MessageType.KICK_RECEIVER.message(target, fPlayer.getFaction().getName(), fPlayer.getName());
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.KICK_KICKER, fPlayer.getName(), target.getName());
    }
}
