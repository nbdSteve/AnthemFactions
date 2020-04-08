package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreateCmd {

    public static void create(CommandSender sender, String[] args) {
        if (!PermissionQueryUtil.hasPermission(sender, "player.create")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.create"));
            return;
        }
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        FactionManager.createFaction(args[1], (Player) sender, UUID.randomUUID());
    }
}
