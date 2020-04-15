package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.XpUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawCmd {

    public static void withdraw(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.WITHDRAW)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.WITHDRAW.get());
            return;
        }
        int withdraw;
        if (args.length == 1) {
            withdraw = fPlayer.getFaction().getXp();
        } else if (args.length == 2) {
            try {
                withdraw = XpUtil.getTotalExperience(Integer.parseInt(args[1]));
            } catch (Exception e) {
                CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
                return;
            }
        } else {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        if (withdraw > fPlayer.getFaction().getXp()) {
            CommandDebug.INSUFFICIENT_XP.message(fPlayer);
            return;
        }
        XpUtil.setTotalExperience(fPlayer, XpUtil.getTotalExperience(fPlayer) + withdraw);
        fPlayer.getFaction().withdrawXp(withdraw);
        MessageType.XP_WITHDRAWAL_SUCCESSFUL.message(fPlayer, AnthemFactions.getNumberFormat().format(withdraw));
    }
}
