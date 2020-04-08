package gg.steve.anthem.utils;

import gg.steve.anthem.managers.FileManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionQueryUtil {

    public static boolean hasPermission(CommandSender sender, String node) {
        return sender.hasPermission(FileManager.get("permissions").getString(node));
    }

    public static boolean hasPermission(Player player, String node) {
        return player.hasPermission(FileManager.get("permissions").getString(node));
    }

    public static String getNode(String node) {
        return FileManager.get("permissions").getString(node);
    }
}
