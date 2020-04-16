package gg.steve.anthem.player;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.raid.FRaid;
import gg.steve.anthem.raid.events.RaidCompletionEvent;
import gg.steve.anthem.raid.events.RaidCompletionType;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && fPlayer.canBuild(event.getBlock().getLocation()) && !fPlayer.hasFactionPermission(PermissionNode.BUILD)) {
            event.setCancelled(true);
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.BUILD.get());
        }
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_RAID.message(fPlayer);
            return;
        }
        if (!fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_FACTION.message(fPlayer, fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && fPlayer.canBuild(event.getBlock().getLocation()) && !fPlayer.hasFactionPermission(PermissionNode.BUILD)) {
            event.setCancelled(true);
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.BUILD.get());
        }
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_RAID.message(fPlayer);
            return;
        }
        if (!fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_FACTION.message(fPlayer, fPlayer.getFactionForCurrentFWorld().getName());
        }
        if (event.getBlock().getType().toString().equalsIgnoreCase("MOB_SPAWNER")) {
            if (fPlayer.isBeingRaided()) {
                event.setCancelled(true);
                MessageType.BREAK_SPAWNER_DURING_RAID.message(fPlayer);
            }
        }
    }

    @EventHandler
    public void bucketPlace(PlayerBucketEmptyEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && fPlayer.canBuild(event.getBlock().getLocation()) && !fPlayer.hasFactionPermission(PermissionNode.BUILD)) {
            event.setCancelled(true);
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.BUILD.get());
        }
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_RAID.message(fPlayer);
            return;
        }
        if (!fPlayer.canBuild(event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_FACTION.message(fPlayer, fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

    @EventHandler
    public void bucketFill(PlayerBucketFillEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && fPlayer.canBuild(event.getBlock().getLocation()) && !fPlayer.hasFactionPermission(PermissionNode.BUILD)) {
            event.setCancelled(true);
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.BUILD.get());
        }
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_RAID.message(fPlayer);
            return;
        }
        if (!fPlayer.canBuild(event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_FACTION.message(fPlayer, fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

    @EventHandler
    public void igniteBlock(BlockIgniteEvent event) {
        if (event.getPlayer() == null) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && fPlayer.canBuild(event.getBlock().getLocation()) && !fPlayer.hasFactionPermission(PermissionNode.BUILD)) {
            event.setCancelled(true);
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.BUILD.get());
        }
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_RAID.message(fPlayer);
            return;
        }
        if (!fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_FACTION.message(fPlayer, fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

    @EventHandler
    public void death(PlayerDeathEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getEntity().getUniqueId());
        if (!fPlayer.isRaiding()) return;
        FRaid raid = fPlayer.getFaction().getfRaid();
        Faction faction = fPlayer.getFaction();
        if (!raid.getRaiders().contains(fPlayer.getUUID().toString())) {
            raid.addRaider(fPlayer.getUUID().toString());
        }
        if (raid.getRaiders().size() == faction.getPlayers().size()) {
            Bukkit.getPluginManager().callEvent(new RaidCompletionEvent(raid,
                    FactionManager.getFaction(raid.getDefendingFaction()),
                    FactionManager.getFaction(raid.getRaidingFaction()),
                    raid.getTier(), RaidCompletionType.RAIDERS_DIE));
        }
    }

    @EventHandler
    public void playerCommand(PlayerCommandPreprocessEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.isRaiding()) return;
        if (!fPlayer.inRaidWorld()) return;
        for (String cmd : FileManager.get("config").getStringList("allowed-cmds-while-raiding")) {
            if (event.getMessage().contains(cmd)) {
                return;
            }
        }
        event.setCancelled(true);
        MessageType.COMMAND_BLOCKED_RAID.message(fPlayer);
    }
}