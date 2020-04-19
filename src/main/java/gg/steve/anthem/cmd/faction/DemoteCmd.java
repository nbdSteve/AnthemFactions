package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DemoteCmd {

    public static void demote(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(sender);
            return;
        }
        Player player = (Player) sender;
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.isBypassed()) {
            if (!fPlayer.hasFactionPermission(PermissionNode.DEMOTE)) {
                MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.DEMOTE.get());
                return;
            }
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CommandDebug.TARGET_NOT_ONLINE.message(fPlayer);
            return;
        }
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (!fPlayer.isBypassed()) {
            if (target.getUniqueId().equals(player.getUniqueId())) {
                CommandDebug.TARGET_CAN_NOT_BE_SELF.message(fPlayer);
                return;
            }
            if (!fPlayer.getFaction().equals(tPlayer.getFaction())) {
                CommandDebug.TARGET_NOT_FACTION_MEMBER.message(fPlayer);
                return;
            }
            if (Role.higherRole(tPlayer.getRole(), fPlayer.getRole())) {
                CommandDebug.DEMOTED_SAME_OR_HIGHER_RANK.message(fPlayer);
                return;
            }
        } else {
            if (target.getUniqueId().equals(player.getUniqueId()) && fPlayer.getRole().equals(Role.OWNER)) {
                CommandDebug.CAN_NOT_DEMOTE_SELF_AS_OWNER.message(fPlayer);
                return;
            }
        }
        if (tPlayer.getRole().equals(Role.MEMBER)) {
            CommandDebug.DEMOTED_ALREADY_MEMBER.message(fPlayer);
            return;
        }
        fPlayer.getFaction().demote(tPlayer.getUUID());
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.DEMOTION, player.getName(), target.getName(), Role.getRoleByWeight(tPlayer.getRole().getWeight() - 1).toString());
        FPlayerManager.updateFPlayer(tPlayer.getUUID());
    }
}
