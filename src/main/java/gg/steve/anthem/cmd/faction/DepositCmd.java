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

public class DepositCmd {

    public static void deposit(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.DEPOSIT)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.DEPOSIT.get());
            return;
        }
        int deposit;
        if (args.length == 1) {
            deposit = XpUtil.getTotalExperience(fPlayer);
        } else if (args.length == 2) {
            try {
                deposit = XpUtil.getTotalExperience(Integer.parseInt(args[1]));
            } catch (Exception e) {
                CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
                return;
            }
        } else {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        if (deposit > XpUtil.getTotalExperience(fPlayer)) {
            CommandDebug.INSUFFICIENT_XP.message(fPlayer);
            return;
        }
        XpUtil.setTotalExperience(fPlayer, XpUtil.getTotalExperience(fPlayer) - deposit);
        fPlayer.getFaction().depositXp(deposit);
        MessageType.XP_DEPOSIT_SUCCESSFUL.message(fPlayer, AnthemFactions.getNumberFormat().format(deposit));
    }
}
