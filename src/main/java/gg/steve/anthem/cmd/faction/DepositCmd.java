package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.PermissionQueryUtil;
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
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.deposit"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.deposit"));
            return;
        }
        double deposit;
        if (args.length == 1) {
            deposit = fPlayer.getPlayer().getExp();
        } else if (args.length == 2) {
            try {
                deposit = Double.parseDouble(args[1]);
            } catch (Exception e) {
                CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
                return;
            }
        } else {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        if (deposit > fPlayer.getPlayer().getExp()) {
            // send insufficient message
            return;
        }
        deposit = XpUtil.levelsToXp(deposit);
        fPlayer.getPlayer().setExp((float) (fPlayer.getPlayer().getExp() - deposit));
        fPlayer.getFaction().depositXp(deposit);
        // send player deposit message
        fPlayer.message("removed " + deposit + " experience into faction bank");
    }
}
