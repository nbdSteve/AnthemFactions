package gg.steve.anthem.cmd;

import gg.steve.anthem.cmd.admin.IdCmd;
import gg.steve.anthem.cmd.sub.*;
import gg.steve.anthem.cmd.unfinished.ListCmd;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FactionsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("f")) {
            if (args.length == 0) {
                MessageUtil.helpMessage(sender);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "create":
                    CreateCmd.create(sender, args);
                    break;
                case "home":
                    HomeCmd.teleportHome(sender);
                    break;
                case "sethome":
                    HomeCmd.setHome(sender);
                    break;
                case "raid":
                    break;
                case "upgrade":
                    break;
                case "perms":
                    break;
                case "disband":
                    DisbandCmd.disband(sender);
                    break;
                case "promote":
                    PromoteCmd.promote(sender, args);
                    break;
                case "demote":
                    DemoteCmd.demote(sender, args);
                    break;
                case "ally":
                    break;
                case "who":
                    break;
                case "list":
                    ListCmd.list(sender);
                    break;
                case "invite":
                    InviteCmd.invite(sender, args);
                    break;
                case "accept":
                    AcceptCmd.accept(sender);
                    break;
                case "kick":
                    KickCmd.kick(sender, args);
                    break;
                case "leave":
                    LeaveCmd.leave(sender);
                    break;
                case "getid":
                    IdCmd.getId(sender, args);
                    break;
                default:
                    MessageUtil.commandDebug(sender, "Invalid initial argument, please check /f help");
            }
        }
        return true;
    }
}
