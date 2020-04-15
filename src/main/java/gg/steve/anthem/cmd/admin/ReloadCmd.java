package gg.steve.anthem.cmd.admin;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.upgrade.crop.CropListener;
import gg.steve.anthem.upgrade.fchest.FChestManager;
import gg.steve.anthem.wealth.AsyncWealthCalculation;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class ReloadCmd {

    public static void reload(CommandSender sender) {
        if (!PermissionNode.RELOAD.hasPermission(sender)) {
            MessageType.PERMISSION_DEBUG.message(sender, PermissionNode.RELOAD.get());
            return;
        }
        FChestManager.saveFChests();
        Bukkit.getScheduler().cancelTasks(AnthemFactions.get());
        FileManager.reload();
        for (UUID uuid : FactionManager.getFactions()) {
            Faction faction = FactionManager.getFaction(uuid);
            faction.getData().reload();
        }
        FactionManager.init();
        FPlayerManager.init();
        CooldownManager.init();
        DelayManager.init();
        CropListener.loadCropConfig();
        AsyncWealthCalculation.init();
        MessageType.RELOAD.message(sender);
    }
}
