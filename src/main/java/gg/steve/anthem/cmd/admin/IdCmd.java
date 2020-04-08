package gg.steve.anthem.cmd.admin;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;

public class IdCmd {

    public static void getId(CommandSender sender, String[] args) {
        if (!PermissionQueryUtil.hasPermission(sender, "admin.id-cmd")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("admin.id-cmd"));
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Incorrect number of arguments");
            return;
        }
        if (FactionManager.getId(args[1]) != null) {
            MessageUtil.message("lang", "faction-id", sender, "{uuid}", FactionManager.getId(args[1]).toString());
        } else {
            MessageUtil.commandDebug(sender, "That faction does not exist");
        }
    }
}
