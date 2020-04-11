package gg.steve.anthem.cmd.admin;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;

public class IdCmd {

    public static void getId(CommandSender sender, String[] args) {
        if (!PermissionQueryUtil.hasPermission(sender, "admin.id-cmd")) {
            MessageType.PERMISSION_DEBUG.message(sender, PermissionQueryUtil.getNode("admin.id-cmd"));
            return;
        }
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(sender);
            return;
        }
        if (FactionManager.getId(args[1]) != null) {
            MessageType.FACTION_ID.message(sender, FactionManager.getId(args[1]).toString());
        } else {
            CommandDebug.FACTION_DOES_NOT_EXIST.message(sender);
        }
    }
}