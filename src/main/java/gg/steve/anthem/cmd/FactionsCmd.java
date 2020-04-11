package gg.steve.anthem.cmd;

import gg.steve.anthem.cmd.admin.IdCmd;
import gg.steve.anthem.cmd.admin.ReloadCmd;
import gg.steve.anthem.cmd.faction.*;
import gg.steve.anthem.cmd.relational.AllyCmd;
import gg.steve.anthem.cmd.misc.ListCmd;
import gg.steve.anthem.cmd.relational.EnemyCmd;
import gg.steve.anthem.cmd.relational.NeutralCmd;
import gg.steve.anthem.cmd.relational.UnAllyCmd;
import gg.steve.anthem.cmd.faction.ChatCmd;
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
                case "home": case "h":
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
                    AllyCmd.ally(sender, args);
                    break;
                case "unally":
                    UnAllyCmd.unAlly(sender, args);
                    break;
                case "enemy":
                    EnemyCmd.enemy(sender, args);
                    break;
                case "neutral":
                    NeutralCmd.neutral(sender, args);
                case "who":
                    break;
                case "list":
                    ListCmd.list(sender);
                    break;
                case "invite": case "i":
                    InviteCmd.invite(sender, args);
                    break;
                case "accept":
                    AcceptCmd.accept(sender);
                    break;
                case "kick":
                    KickCmd.kick(sender, args);
                    break;
                case "tag":
                    TagCmd.tag(sender, args);
                    break;
                case "leave": case "l":
                    LeaveCmd.leave(sender);
                    break;
                case "getid":
                    IdCmd.getId(sender, args);
                    break;
                case "chat": case "c":
                    ChatCmd.chat(sender, args);
                    break;
                case "reload": case "r":
                    ReloadCmd.reload(sender);
                    break;
                default:
                    MessageUtil.commandDebug(sender, "Invalid initial argument, please check /f help");
            }
        }
        return true;
    }
}
