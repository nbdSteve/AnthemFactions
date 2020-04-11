package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.exception.CooldownActiveException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCmd {

    public static void invite(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        Player player = (Player) sender;
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you must create a faction using /f create {name} first");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.invite"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.invite"));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageUtil.commandDebug(sender, "Error, the player you are inviting must be online");
            return;
        }
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (target.getUniqueId().equals(player.getUniqueId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot invite yourself");
            return;
        }
        if (fPlayer.getFaction().equals(tPlayer.getFaction())) {
            MessageUtil.commandDebug(sender, "Error, that player is already a member of your faction");
            return;
        }
        try {
            CooldownManager.addCooldown(target.getUniqueId(), new Cooldown(CooldownType.INVITE, fPlayer.getFaction()));
        } catch (CooldownActiveException e) {
            MessageUtil.commandDebug(sender, "Error, that player has already a pending invite");
            return;
        }
        MessageType.INVITE_RECEIVER.message(tPlayer, fPlayer.getFaction().getName(), player.getName());
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.INVITE_SENDER, player.getName(), target.getName());
    }
}
