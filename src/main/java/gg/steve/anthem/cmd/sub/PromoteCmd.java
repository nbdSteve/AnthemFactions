package gg.steve.anthem.cmd.sub;

import com.sun.prism.shader.FillPgram_LinearGradient_PAD_AlphaTest_Loader;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PromoteCmd {

    public static void promote(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.commandDebug(sender, "Error, only players can invite others to factions");
            return;
        }
//        if (!PermissionQueryUtil.hasPermission(sender, "player.invite")) {
//            MessageUtil.permissionDebug(sender, PermissionQueryUtil.getNode("player.invite"));
//            return;
//        }
        if (args.length != 2) {
            MessageUtil.commandDebug(sender, "Invalid number of arguments");
            return;
        }
        Player player = (Player) sender;
        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
        if (fPlayer.getFaction().getId().equals(FactionManager.getWildernessId())) {
            MessageUtil.commandDebug(sender, "Error, you must create a faction using /f create {name} first");
            return;
        }
        if (!fPlayer.hasFactionPermission("factions.player.promote")) {
            MessageUtil.message("lang", "insufficient-role-permission", player);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageUtil.commandDebug(sender, "Error, the player you are promoting must be online");
            return;
        }
        FPlayer tPlayer = FPlayerManager.getFPlayer(target.getUniqueId());
        if (target.getUniqueId().equals(player.getUniqueId())) {
            MessageUtil.commandDebug(sender, "Error, you cannot promote yourself");
            return;
        }
        if (!fPlayer.getFaction().equals(tPlayer.getFaction())) {
            MessageUtil.commandDebug(sender, "Error, you cannot promote someone who is not in your faction");
            return;
        }
        if (Role.higherRole(tPlayer.getRole(), fPlayer.getRole())) {
            MessageUtil.commandDebug(sender, "Error, you cannot promote someone who is a higher, or the same rank as you");
            return;
        }
        for (UUID uuid : fPlayer.getFaction().getPlayers()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member.getUniqueId().equals(tPlayer.getUUID())) continue;
            MessageUtil.message("lang", "promotion-alert", member, "{promoter}", player.getName(), "{promoted}", target.getName(), "{role}", Role.getRoleByWeight(tPlayer.getRole().getWeight() + 1).toString());
        }
        fPlayer.getFaction().promote(tPlayer.getUUID());
        FPlayerManager.updateFPlayer(tPlayer.getUUID());
        tPlayer = FPlayerManager.getFPlayer(tPlayer.getUUID());
        MessageUtil.message("lang", "promotion", tPlayer.getPlayer(), "{promoter}", player.getName(), "{role}", tPlayer.getRole().toString());
    }
}
