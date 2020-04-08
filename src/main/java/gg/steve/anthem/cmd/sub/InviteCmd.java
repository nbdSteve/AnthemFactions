package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.exception.CooldownActiveException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import gg.steve.anthem.utils.PermissionQueryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InviteCmd {

    public static void invite(CommandSender sender, String[] args) {
        if (!PermissionQueryUtil.hasPermission(sender, "player.invite")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.invite"));
            return;
        }
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
            return;
        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[1]);
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you must create a faction using /f create {name} first");
            return;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot invite yourself");
            return;
        }
        if (!target.isOnline()) {
            MessageUtil.commandDebug(sender, "Error, the player you are inviting must be online");
            return;
        }
        if (fPlayer.getRole().equals(Role.MEMBER)) {
            MessageUtil.message("lang", "insufficient-role-permission", player);
            return;
        }
        try {
            CooldownManager.addCooldown(target.getUniqueId(), new Cooldown(CooldownType.INVITE, 60, fPlayer.getFaction()));
        } catch (CooldownActiveException e) {
            MessageUtil.commandDebug(sender, "Error, that player has already a pending invite");
            return;
        }
        MessageUtil.message("lang", "faction-invite-receiver", target, "{inviter}", player.getName(), "{faction-name}", fPlayer.getFaction().getName());
        for (UUID uuid : fPlayer.getFaction().getPlayers()) {
            Player member = Bukkit.getPlayer(uuid);
            MessageUtil.message("lang", "faction-invite-inviter", member, "{inviter}", player.getName(), "{invited}", target.getName());
        }
    }

    public static void accept(CommandSender sender) {
        if (!PermissionQueryUtil.hasPermission(sender, "player.accept")) {
            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.accept"));
            return;
        }
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
        if (!CooldownManager.isOnCooldown(uuid, CooldownType.INVITE)) {
            MessageUtil.commandDebug(sender, "Error, you do not have a pending faction invite");
            return;
        }
        Faction faction = CooldownManager.getCooldown(uuid, CooldownType.INVITE).getFaction();
        for (UUID memberUUID : faction.getPlayers()) {
            Player member = Bukkit.getPlayer(memberUUID);
            MessageUtil.message("lang", "new-member-join", member, "{member}", player.getName());
        }
        faction.addPlayer(uuid, Role.MEMBER);
        FPlayerManager.updateFPlayer(uuid);
        CooldownManager.removeCooldown(uuid, CooldownType.INVITE);
        MessageUtil.message("lang", "join", player, "{faction-name}", faction.getName());
    }
}
