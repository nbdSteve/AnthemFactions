package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.cmd.MessageType;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.exception.DelayAlreadyActiveException;
import gg.steve.anthem.exception.NotOnDelayException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCmd {

    public static void teleportHome(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can teleport to their faction home");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (fPlayer.getFaction().equals(FactionManager.getWilderness())) {
            MessageUtil.commandDebug(fPlayer, "You are not a member of any faction");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.home"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.home"));
            return;
        }
        if (DelayManager.onDelay(fPlayer.getUUID(), CooldownType.CREATE_TELEPORT)) {
            try {
                DelayManager.getDelay(fPlayer.getUUID(), CooldownType.CREATE_TELEPORT).messageCountdown(fPlayer.getUUID());
            } catch (NotOnDelayException e) {
                e.printStackTrace();
            }
        }
        try {
            DelayManager.addDelay(fPlayer.getUUID(), CooldownType.HOME_TELEPORT, fPlayer.getFaction().getHome());
        } catch (DelayAlreadyActiveException e) {
            e.printStackTrace();
        }
    }

    public static void setHome(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can set their faction home");
            return;
        }
        Player player = (Player) sender;
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        if (FactionManager.getFaction(fPlayer).equals(FactionManager.getWilderness())) {
            MessageUtil.commandDebug(fPlayer, "You are not a member of any faction");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.set-home"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.set-home"));
            return;
        }
        fPlayer.getFaction().setHome(fPlayer.getPlayer().getLocation());
        MessageType.SET_HOME.message(fPlayer);
    }
}
