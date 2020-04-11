package gg.steve.anthem.player;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.MessageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
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
    public void bucketPlace(PlayerBucketEmptyEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
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
    public void playerCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer() == null) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getPlayer().getLocation())) {
            for (String cmd : FileManager.get("config").getStringList("blocked-commands.raid-territory")) {
                if (event.getMessage().contains(cmd)) {
                    event.setCancelled(true);
                    break;
                }
            }
            MessageType.COMMAND_BLOCKED_RAID.message(fPlayer);
            return;
        }
        if (!fPlayer.canBuild(event.getPlayer().getLocation())) {
            for (String cmd : FileManager.get("config").getStringList("blocked-commands.faction-territory")) {
                if (event.getMessage().contains(cmd)) {
                    event.setCancelled(true);
                    break;
                }
            }
            MessageType.COMMAND_BLOCKED_FACTION_TERRITORY.message(fPlayer, fPlayer.getFactionForCurrentFWorld().getName());
        }
    }
}