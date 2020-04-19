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
import gg.steve.anthem.world.FWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerEventListener implements Listener {

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isBypassed()) return;
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
        if (fPlayer.isBypassed()) return;
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
    public void bucketEvents(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!event.getMaterial().toString().contains("BUCKET")) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isBypassed()) return;
        if (fPlayer.isInHomeWorld() && fPlayer.canBuild(event.getClickedBlock().getLocation()) && !fPlayer.hasFactionPermission(PermissionNode.BUILD)) {
            event.setCancelled(true);
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.BUILD.get());
        }
        if (fPlayer.isInHomeWorld() && !fPlayer.canBuild(event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_RAID.message(fPlayer);
            return;
        }
        if (!fPlayer.canBuild(event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
            MessageType.BUILD_BLOCKED_FACTION.message(fPlayer, fPlayer.getFactionForCurrentFWorld().getName());
        }
    }

    @EventHandler
    public void igniteBlock(BlockIgniteEvent event) {
        if (event.getPlayer() == null) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isBypassed()) return;
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
    public void playerDeath(PlayerDeathEvent event) {
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
    public void playerCommandRaid(PlayerCommandPreprocessEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isBypassed()) return;
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

    @EventHandler
    public void playerCommandNormal(PlayerCommandPreprocessEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isBypassed()) return;
        if (fPlayer.isRaiding()) return;
        if (!fPlayer.inFactionWorld()) return;
        if (event.getMessage().equalsIgnoreCase("/f sethome")) return;
        for (String cmd : FileManager.get("config").getStringList("cmds-blocked-in-faction-worlds")) {
            if (event.getMessage().contains(cmd)) {
                event.setCancelled(true);
                MessageType.COMMAND_BLOCKED_FACTION_WORLD.message(fPlayer);
                return;
            }
        }
    }

    @EventHandler
    public void pistonPush(BlockPistonExtendEvent event) {
        Location start = event.getBlock().getLocation();
        FWorld fWorld = null;
        for (Player player : event.getBlock().getWorld().getPlayers()) {
            FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
            if (fPlayer.getFWorld() != null) {
                fWorld = fPlayer.getFWorld();
            }
            break;
        }
        if (fWorld == null) return;
        boolean faction = fWorld.inFactionLand(start);
        Block end;
        if (event.getBlocks().size() > 0) {
            end = event.getBlocks().get(event.getBlocks().size() - 1).getRelative(event.getDirection());
        } else {
            end = event.getBlock().getRelative(event.getDirection());
        }
        if (faction) {
            if (!fWorld.inFactionLand(end.getLocation())) {
                event.setCancelled(true);
                return;
            }
        } else if (fWorld.inFactionLand(end.getLocation())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void pistonPush(BlockPistonRetractEvent event) {
        Location start = event.getBlock().getLocation();
        FWorld fWorld = null;
        for (Player player : event.getBlock().getWorld().getPlayers()) {
            FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
            if (fPlayer.getFWorld() != null) {
                fWorld = fPlayer.getFWorld();
            }
            break;
        }
        if (fWorld == null) return;
        boolean faction = fWorld.inFactionLand(start);
        for (Block block : event.getBlocks()) {
            if (faction) {
                if (!fWorld.inFactionLand(block.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            } else if (fWorld.inFactionLand(block.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }
    }
}