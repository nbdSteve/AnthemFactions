package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.CommandDebug;
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
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.invite"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.invite"));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CommandDebug.TARGET_NOT_ONLINE.message(fPlayer);
            return;
        }
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (target.getUniqueId().equals(fPlayer.getUUID())) {
            CommandDebug.TARGET_CAN_NOT_BE_SELF.message(fPlayer);
            return;
        }
        if (fPlayer.getFaction().equals(tPlayer.getFaction())) {
            CommandDebug.TARGET_ALREADY_MEMBER.message(fPlayer);
            return;
        }
        try {
            CooldownManager.addCooldown(target.getUniqueId(), new Cooldown(CooldownType.INVITE, fPlayer.getFaction()));
        } catch (CooldownActiveException e) {
            CommandDebug.INVITE_ALREADY_PENDING.message(fPlayer);
            return;
        }
        MessageType.INVITE_RECEIVER.message(tPlayer, fPlayer.getFaction().getName(), fPlayer.getName());
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.INVITE_SENDER, fPlayer.getName(), target.getName());
    }
}
