package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromoteCmd {

    public static void promote(CommandSender sender, String[] args) {
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
        if (!fPlayer.isBypassed()) {
            if (!fPlayer.hasFactionPermission(PermissionNode.PROMOTE)) {
                MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.PROMOTE.get());
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
            if (target.getUniqueId().equals(fPlayer.getUUID())) {
                CommandDebug.TARGET_CAN_NOT_BE_SELF.message(fPlayer);
                return;
            }
            if (!fPlayer.getFaction().equals(tPlayer.getFaction())) {
                CommandDebug.TARGET_NOT_FACTION_MEMBER.message(fPlayer);
                return;
            }
            if (Role.higherRole(tPlayer.getRole(), fPlayer.getRole())) {
                CommandDebug.PROMOTE_SAME_OR_HIGHER_RANK.message(fPlayer);
                return;
            }
        } else {
            if (fPlayer.getRole().equals(Role.OWNER) && target.getUniqueId().equals(fPlayer.getUUID())) {
                CommandDebug.ALREADY_OWNER.message(fPlayer);
                return;
            }
            if (tPlayer.getRole().equals(Role.OWNER)) {
                CommandDebug.ALREADY_OWNER.message(fPlayer);
                return;
            }
        }
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.PROMOTION, fPlayer.getName(), target.getName(), Role.getRoleByWeight(tPlayer.getRole().getWeight() + 1).toString());
        fPlayer.getFaction().promote(tPlayer.getUUID());
        FPlayerManager.updateFPlayer(tPlayer.getUUID());
    }
}
