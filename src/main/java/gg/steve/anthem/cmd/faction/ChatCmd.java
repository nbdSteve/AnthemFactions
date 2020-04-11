package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.chat.ChatType;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCmd {

    public static void chat(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (args.length != 2) {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        try {
            ChatType.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            CommandDebug.INVALID_CHAT_CHANNEL.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFaction()) {
            CommandDebug.CHAT_NO_FACTION.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.chat-" + args[1]))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.chat-" + args[1]));
            return;
        }
        fPlayer.setChatChannel(ChatType.valueOf(args[1].toUpperCase()));
        MessageType.CHANGE_CHANNEL.message(fPlayer, args[1].toUpperCase());
    }
}