package gg.steve.anthem.cmd.misc;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.wealth.AsyncWealthCalculation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCmd {

    public static void top(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFactionPermission(PermissionNode.TOP)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.TOP.get());
            return;
        }
        int page;
        if (args.length == 1) {
            page = 1;
        } else if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
                return;
            }
        } else {
            CommandDebug.INCORRECT_ARGUMENTS.message(fPlayer);
            return;
        }
        MessageType.TOP_HEADER.message(fPlayer, String.valueOf(page));
        for (int i = (page - 1) * 9; i <= ((page - 1) * 9 + 8); i++) {
            if (i >= AsyncWealthCalculation.getFactionsInWealthOrder().size()) break;
            Faction faction = AsyncWealthCalculation.getFactionsInWealthOrder().get(i);
            if (faction.getId().equals(FactionManager.getWildernessId())) continue;
            RelationType type = faction.getRelationManager().getRelationType(fPlayer.getFaction());
            if (type.equals(RelationType.WILDERNESS)) {
                type = RelationType.NEUTRAL;
            }
            MessageType.TOP_ENTRY.message(fPlayer, faction.getName(), type.getPrefix(), String.valueOf(i + 1), String.valueOf(faction.getWealth()));
        }
        MessageType.TOP_FOOTER.message(fPlayer);
    }
}
