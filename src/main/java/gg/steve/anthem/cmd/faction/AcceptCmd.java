package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.cooldown.exception.NotOnCooldownException;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptCmd {

    public static void accept(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        FPlayer fPlayer = FPlayerManager.getFPlayer(uuid);
        if (!fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            CommandDebug.CAN_NOT_ACCEPT_INVITE.message(fPlayer);
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionNode.ACCEPT)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.ACCEPT.get());
            return;
        }
        if (!CooldownManager.isOnCooldown(uuid, CooldownType.INVITE)) {
            CommandDebug.NO_INVITE_PENDING.message(fPlayer);
            return;
        }
        Faction faction = null;
        try {
            faction = CooldownManager.getCooldown(uuid, CooldownType.INVITE).getFaction();
        } catch (NotOnCooldownException e) {
            e.printStackTrace();
        }
        faction.messageAllOnlinePlayers(MessageType.MEMBER_JOIN_ALERT, player.getName());
        faction.addPlayer(uuid, Role.MEMBER);
        FPlayerManager.updateFPlayer(uuid);
        try {
            CooldownManager.removeCooldown(uuid, CooldownType.INVITE);
        } catch (NotOnCooldownException e) {
            e.printStackTrace();
        }
        MessageType.JOIN.message(player, faction.getName());
    }
}
