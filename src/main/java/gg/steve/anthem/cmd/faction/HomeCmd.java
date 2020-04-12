package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.exception.DelayAlreadyActiveException;
import gg.steve.anthem.exception.NotOnDelayException;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCmd {

    public static void teleportHome(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
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
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        Player player = (Player) sender;
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        if (FactionManager.getFaction(fPlayer).equals(FactionManager.getWilderness())) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.set-home"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.set-home"));
            return;
        }
        if (fPlayer.isInHomeWorld() && fPlayer.canBuild(fPlayer.getPlayer().getLocation())) {
            fPlayer.getFaction().setHome(fPlayer.getPlayer().getLocation());
            MessageType.SET_HOME.message(fPlayer);
        } else {
            CommandDebug.SET_HOME_NOT_IN_TERRITORY.message(fPlayer);
        }
    }
}
