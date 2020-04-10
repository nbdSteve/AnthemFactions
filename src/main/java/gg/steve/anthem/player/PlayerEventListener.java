package gg.steve.anthem.player;

import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageUtil.message("lang", "build-blocked-raid", event.getPlayer());
            return;
        }
        if (!fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageUtil.message("lang", "build-blocked-faction", event.getPlayer(), "{faction-name}", fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageUtil.message("lang", "build-blocked-raid", event.getPlayer());
            return;
        }
        if (!fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageUtil.message("lang", "build-blocked-faction", event.getPlayer(), "{faction-name}", fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

    @EventHandler
    public void igniteBlock(BlockIgniteEvent event) {
        if (event.getPlayer() == null) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (!fPlayer.inFactionWorld()) return;
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageUtil.message("lang", "build-blocked-raid", event.getPlayer());
            return;
        }
        if (!fPlayer.canBuild(event.getBlock().getLocation())) {
            event.setCancelled(true);
            MessageUtil.message("lang", "build-blocked-faction", event.getPlayer(), "{faction-name}", fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

//    @EventHandler
//    public void teleport(PlayerTeleportEvent event) {
//        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
//        if (fPlayer.isInHomeWorld()) return;
//        if (!fPlayer.canBuild(event.getPlayer().getLocation())) {
//            event.setCancelled(true);
//            MessageType.message("lang", "teleport-blocked-faction", event.getPlayer(), "{faction-name}", fPlayer.getFactionForCurrentFWorld().getName());
//        }
//    }
}
