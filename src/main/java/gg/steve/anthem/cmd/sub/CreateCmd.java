package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.exception.CooldownActiveException;
import gg.steve.anthem.exception.NotOnCooldownException;
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
        if (!fPlayer.hasFactionPermission("factions.player.create")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.create"));
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        if (fPlayer.hasFaction()) {
            MessageUtil.commandDebug(sender, "Error, you must leave or disband your current faction first");
            return;
        }
        UUID uuid = ((Player) sender).getUniqueId();
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
    }
}
