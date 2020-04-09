package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.core.FactionManager;
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
        if (!fPlayer.hasFactionPermission("factions.player.tag")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.tag"));
            return;
        }
        if (!FactionManager.changeTag(fPlayer.getFaction(), args[1])) {
            MessageUtil.commandDebug(sender, "Error, a faction with that name already exists");
            return;
        }
        fPlayer.getFaction().messageAllOnlinePlayers("lang", "faction-tag-change", "{changer}", fPlayer.getPlayer().getName(), "{new-name}", fPlayer.getFaction().getName());
    }
}
