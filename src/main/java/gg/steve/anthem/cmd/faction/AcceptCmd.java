package gg.steve.anthem.cmd.faction;

import gg.steve.anthem.cmd.MessageType;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.exception.NotOnCooldownException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptCmd {

    public static void accept(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
            return;
        }
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        FPlayer fPlayer = FPlayerManager.getFPlayer(uuid);
        if (!fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you must leave you current faction using /f leave first");
            return;
        }
        if (!fPlayer.hasFactionPermission(PermissionQueryUtil.getNode("player.accept"))) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionQueryUtil.getNode("player.accept"));
            return;
        }
        if (!CooldownManager.isOnCooldown(uuid, CooldownType.INVITE)) {
            MessageUtil.commandDebug(sender, "Error, you do not have a pending faction invite");
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
