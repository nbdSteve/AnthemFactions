package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCmd {

    public static void tag(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you must create a faction using /f create {name} first");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.tag"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.tag"));
            return;
        }
        if (!FactionManager.changeTag(fPlayer.getFaction(), args[1])) {
            MessageUtil.commandDebug(sender, "Error, a faction with that name already exists");
            return;
        }
        if (args[1].length() < FileManager.get("config").getInt("minimum-tag-length")) {
            MessageUtil.commandDebug(sender, "Error, your faction tag cannot be shorter than " + FileManager.get("config").getInt("minimum-tag-length") + " characters");
            return;
        }
        if (args[1].length() > FileManager.get("config").getInt("maximum-tag-length")) {
            MessageUtil.commandDebug(sender, "Error, your faction tag cannot be longer than " + FileManager.get("config").getInt("maximum-tag-length") + " characters");
            return;
        }
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.TAG_CHANGE, fPlayer.getFaction().getName(), fPlayer.getPlayer().getName());
    }
}
