package gg.steve.anthem.cmd.misc;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import org.bukkit.command.CommandSender;

public class HelpCmd {

    public static void help(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                CommandDebug.INCORRECT_ARGUMENTS.message(sender);
                return;
            }
            if (page > FileManager.get("help").getInt("total-pages")) {
                CommandDebug.INVALID_PAGE.message(sender);
            }
        }
        MessageType.HELP.setPath(String.valueOf(page));
        MessageType.HELP.message(sender, String.valueOf(page));
    }
}
