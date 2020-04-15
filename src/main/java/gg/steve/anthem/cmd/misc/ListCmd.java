package gg.steve.anthem.cmd.misc;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCmd {

    public static void list(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFactionPermission(PermissionNode.LIST)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.LIST.get());
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
        MessageType.LIST_HEADER.message(fPlayer, String.valueOf(page));
        for (int i = (page - 1) * 9; i <= ((page - 1) * 9 + 8); i++) {
            if (i >= FactionManager.getFactions().size()) break;
            Faction faction = FactionManager.getFactionsAsList().get(i);
            LogUtil.info(faction.getName() + " "+ i);
            if (faction.getId().equals(FactionManager.getWildernessId())) continue;
            RelationType type = faction.getRelationManager().getRelationType(fPlayer.getFaction());
            if (type.equals(RelationType.WILDERNESS)) {
                type = RelationType.NEUTRAL;
            }
            MessageType.LIST_ENTRY.message(fPlayer, faction.getName(), type.getPrefix(), type.toString(),
                    faction.getNumberOnline(), faction.getTotalPlayers(), faction.getOwnerAsPlayer().getName());
        }
        MessageType.LIST_FOOTER.message(fPlayer);
    }
}
