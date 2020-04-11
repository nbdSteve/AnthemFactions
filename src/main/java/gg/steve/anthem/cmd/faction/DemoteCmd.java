package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DemoteCmd {

    public static void demote(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_DEMOTE.message(sender);
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
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.demote"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.demote"));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CommandDebug.TARGET_NOT_ONLINE.message(fPlayer);
            return;
        }
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (target.getUniqueId().equals(player.getUniqueId())) {
            CommandDebug.TARGET_CAN_NOT_BE_SELF.message(fPlayer);
            return;
        }
        if (!fPlayer.getFaction().equals(tPlayer.getFaction())) {
            CommandDebug.DEMOTED_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (Role.higherRole(tPlayer.getRole(), fPlayer.getRole())) {
            MessageUtil.commandDebug(sender, "Error, you cannot demote someone who is a higher, or the same rank as you");
            return;
        }
        if (tPlayer.getRole().equals(Role.MEMBER)) {
            MessageUtil.commandDebug(sender, "Error, that player is already a the rank MEMBER and cannot be demoted further. Use /f kick {name} to remove them from the faction");
            return;
        }
        fPlayer.getFaction().demote(tPlayer.getUUID());
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.DEMOTION, player.getName(), target.getName(), Role.getRoleByWeight(tPlayer.getRole().getWeight() - 1).toString());
        FPlayerManager.updateFPlayer(tPlayer.getUUID());
    }
}
