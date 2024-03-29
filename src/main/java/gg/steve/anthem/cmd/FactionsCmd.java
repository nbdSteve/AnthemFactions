package gg.steve.anthem.cmd;

import gg.steve.anthem.cmd.admin.BypassCmd;
import gg.steve.anthem.cmd.admin.ForceJoinCmd;
import gg.steve.anthem.cmd.admin.IdCmd;
import gg.steve.anthem.cmd.admin.ReloadCmd;
import gg.steve.anthem.cmd.faction.*;
import gg.steve.anthem.cmd.misc.HelpCmd;
import gg.steve.anthem.cmd.misc.ListCmd;
import gg.steve.anthem.cmd.misc.TopCmd;
import gg.steve.anthem.cmd.misc.WhoCmd;
import gg.steve.anthem.cmd.raid.RaidCmd;
import gg.steve.anthem.cmd.relational.AllyCmd;
import gg.steve.anthem.cmd.relational.EnemyCmd;
import gg.steve.anthem.cmd.relational.NeutralCmd;
import gg.steve.anthem.cmd.relational.UnAllyCmd;
import gg.steve.anthem.cmd.upgrade.FChestCmd;
import gg.steve.anthem.cmd.upgrade.FFlyCmd;
import gg.steve.anthem.cmd.upgrade.UpgradeCmd;
import gg.steve.anthem.message.CommandDebug;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FactionsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("f")) {
            if (args.length == 0) {
                HelpCmd.help(sender, args);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "create":
                    CreateCmd.create(sender, args);
                    break;
                case "home":
                case "h":
                    HomeCmd.teleportHome(sender);
                    break;
                case "sethome":
                    HomeCmd.setHome(sender);
                    break;
                case "raid":
                    RaidCmd.raid(sender, args);
                    break;
                case "upgrade":
                    UpgradeCmd.upgradeCmd(sender);
                    break;
                case "perms":
                    PermsCmd.perms(sender);
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
                    break;
                case "who":
                case "show":
                    WhoCmd.who(sender, args);
                    break;
                case "list":
                    ListCmd.list(sender, args);
                    break;
                case "invite":
                case "i":
                    InviteCmd.invite(sender, args);
                    break;
                case "accept":
                case "join":
                    AcceptCmd.accept(sender);
                    break;
                case "kick":
                    KickCmd.kick(sender, args);
                    break;
                case "tag":
                    TagCmd.tag(sender, args);
                    break;
                case "leave":
                case "l":
                    LeaveCmd.leave(sender);
                    break;
                case "getid":
                case "id":
                    IdCmd.getId(sender, args);
                    break;
                case "chat":
                case "c":
                    ChatCmd.chat(sender, args);
                    break;
                case "reload":
                case "r":
                    ReloadCmd.reload(sender);
                    break;
                case "top":
                case "t":
                    TopCmd.top(sender, args);
                    break;
                case "deposit":
                case "d":
                    DepositCmd.deposit(sender, args);
                    break;
                case "withdraw":
                case "w":
                    WithdrawCmd.withdraw(sender, args);
                    break;
                case "xp":
                    XpCmd.xp(sender);
                    break;
                case "chest":
                case "vault":
                case "v":
                    FChestCmd.chest(sender);
                    break;
                case "fly":
                case "f":
                    FFlyCmd.fly(sender);
                    break;
                case "bypass":
                    BypassCmd.bypass(sender);
                    break;
                case "help":
                    HelpCmd.help(sender, args);
                    break;
                case "fjoin":
                    ForceJoinCmd.join(sender, args);
                    break;
                default:
                    CommandDebug.INCORRECT_ARGUMENTS.message(sender);
            }
        }
        return true;
    }
}
