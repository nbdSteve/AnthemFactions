package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.cmd.MessageType;
import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.exception.CooldownActiveException;
import gg.steve.anthem.exception.DelayAlreadyActiveException;
import gg.steve.anthem.exception.NotOnCooldownException;
import gg.steve.anthem.exception.NotOnDelayException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreateCmd {

    public static void create(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        UUID uuid = fPlayer.getUUID();
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.create"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.create"));
            return;
        }
        if (DelayManager.onDelay(uuid, CooldownType.CREATE_TELEPORT)) {
            try {
                DelayManager.getDelay(uuid, CooldownType.CREATE_TELEPORT).messageCountdown(uuid);
            } catch (NotOnDelayException e) {
                e.printStackTrace();
            }
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        if (fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you must leave or disband your current faction first");
            return;
        }
        if (CooldownManager.isOnCooldown(uuid, CooldownType.CREATE)) {
            try {
                CooldownManager.getCooldown(uuid, CooldownType.CREATE).message(uuid);
            } catch (NotOnCooldownException e) {
                e.printStackTrace();
            }
            return;
        }
        if (FactionManager.factionAlreadyExists(args[1])) {
            MessageUtil.commandDebug(sender, "Error, a faction with that name already exists");
            return;
        }
        try {
            CooldownManager.addCooldown(uuid, new Cooldown(CooldownType.CREATE));
        } catch (CooldownActiveException e) {
            e.printStackTrace();
        }
        FactionManager.createFaction(args[1], (Player) sender, UUID.randomUUID());
        FPlayerManager.updateFPlayer(fPlayer.getUUID());
        fPlayer = FPlayerManager.getFPlayer(fPlayer.getUUID());
        try {
            DelayManager.addDelay(uuid, CooldownType.CREATE_TELEPORT, FactionManager.getFWorld(fPlayer.getFaction().getId()).getSpawnLocation());
        } catch (DelayAlreadyActiveException e) {
            e.printStackTrace();
        }
    }
}
