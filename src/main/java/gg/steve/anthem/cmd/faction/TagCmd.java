package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCmd {

    public static void tag(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.TAG)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.TAG.get());
            return;
        }
        if (!FactionManager.changeTag(fPlayer.getFaction(), args[1])) {
            CommandDebug.FACTION_ALREADY_EXISTS.message(fPlayer);
            return;
        }
        if (args[1].length() < FileManager.get("config").getInt("minimum-tag-length")) {
            CommandDebug.MINIMUM_TAG_LENGTH.message(fPlayer, String.valueOf(FileManager.get("config").getInt("minimum-tag-length")));
            return;
        }
        if (args[1].length() > FileManager.get("config").getInt("maximum-tag-length")) {
            CommandDebug.MAXIMUM_TAG_LENGTH.message(fPlayer, String.valueOf(FileManager.get("config").getInt("maximum-tag-length")));
            return;
        }
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.TAG_CHANGE, fPlayer.getFaction().getName(), fPlayer.getPlayer().getName());
    }
}
