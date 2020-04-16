package gg.steve.anthem.raid;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionDataFileUtil;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.BroadcastType;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.raid.events.RaidCompletionEvent;
import gg.steve.anthem.raid.events.RaidCompletionType;
import gg.steve.anthem.raid.events.RaidStartEvent;
import gg.steve.anthem.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FRaid implements Listener {
    private Faction defendingFaction;
    private Faction raidingFaction;
    private Tier tier;
    private BukkitTask task;
    private int remaining;
    private List<UUID> offlineRaiders;

    public FRaid(Faction defendingFaction, Faction raidingFaction, Tier tier) {
        this.defendingFaction = defendingFaction;
        this.raidingFaction = raidingFaction;
        this.tier = tier;
        this.remaining = tier.getDuration();
        this.offlineRaiders = new ArrayList<>();
        Bukkit.getPluginManager().callEvent(new RaidStartEvent(defendingFaction, raidingFaction, tier));
    }

    public FRaid(Faction defendingFaction, Faction raidingFaction, Tier tier, int remaining) {
        this.defendingFaction = defendingFaction;
        this.raidingFaction = raidingFaction;
        this.tier = tier;
        this.remaining = remaining;
        this.offlineRaiders = new ArrayList<>();
        Bukkit.getPluginManager().callEvent(new RaidStartEvent(defendingFaction, raidingFaction, tier));
    }

    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(AnthemFactions.get(), () -> {
            if (this.remaining > 0) {
                switch (this.remaining) {
                    case 2700:
                    case 1800:
                    case 900:
                    case 600:
                    case 300:
                        // message both factions time remaining
                        TimeUtil timeUtil = new TimeUtil(remaining);
                        this.defendingFaction.messageAllOnlinePlayers(MessageType.RAID_TIME_UPDATE, timeUtil.getDays(), timeUtil.getHours(), timeUtil.getMinutes(), timeUtil.getSeconds());
                        this.raidingFaction.messageAllOnlinePlayers(MessageType.RAID_TIME_UPDATE, timeUtil.getDays(), timeUtil.getHours(), timeUtil.getMinutes(), timeUtil.getSeconds());
                        break;
                }
                this.remaining--;
                return;
            }
            Bukkit.getPluginManager().callEvent(new RaidCompletionEvent(defendingFaction, raidingFaction, tier, RaidCompletionType.TIMEOUT));
            this.task.cancel();
        }, 0L, 20L);
    }

    public boolean isActive() {
        return this.remaining > 0;
    }

    public void saveToFile() {
        if (!isActive()) return;
        FactionDataFileUtil defendingData = this.defendingFaction.getData();
        defendingData.get().set("raid.active-raid.active", true);
        defendingData.get().set("raid.active-raid.raiding-faction", this.raidingFaction.getId().toString());
        defendingData.get().set("raid.active-raid.defending-faction", this.defendingFaction.getId().toString());
        defendingData.get().set("raid.active-raid.tier", this.tier.toString());
        defendingData.get().set("raid.active-raid.active", this.remaining);
        defendingData.save();
        FactionDataFileUtil raidingData = this.raidingFaction.getData();
        raidingData.get().set("raid.active-raid.active", true);
        raidingData.get().set("raid.active-raid.raiding-faction", this.raidingFaction.getId().toString());
        raidingData.get().set("raid.active-raid.defending-faction", this.defendingFaction.getId().toString());
        raidingData.get().set("raid.active-raid.tier", this.tier.toString());
        raidingData.get().set("raid.active-raid.active", this.remaining);
        raidingData.save();
    }

    public void resetRaidData() {
        if (isActive()) return;
        FactionDataFileUtil defendingData = this.defendingFaction.getData();
        defendingData.get().set("raid.active-raid.active", false);
        defendingData.get().set("raid.active-raid.raiding-faction", "null");
        defendingData.get().set("raid.active-raid.defending-faction", "null");
        defendingData.get().set("raid.active-raid.tier", "null");
        defendingData.get().set("raid.active-raid.active", 0);
        defendingData.save();
        FactionDataFileUtil raidingData = this.raidingFaction.getData();
        raidingData.get().set("raid.active-raid.active", true);
        raidingData.get().set("raid.active-raid.raiding-faction", "null");
        raidingData.get().set("raid.active-raid.defending-faction", "null");
        raidingData.get().set("raid.active-raid.tier", "null");
        raidingData.get().set("raid.active-raid.active", 0);
        raidingData.save();
    }

    public UUID getRaidingFaction() {
        return this.raidingFaction.getId();
    }

    public UUID getDefendingFaction() {
        return this.defendingFaction.getId();
    }

    public boolean isOfflineRaider(UUID uuid) {
        return this.offlineRaiders.contains(uuid);
    }

    public void removeOfflineRaider(UUID uuid) {
        this.offlineRaiders.remove(uuid);
    }

    public List<UUID> getOfflineRaiders() {
        return offlineRaiders;
    }

    public void teleportToRaid(UUID uuid, Faction defendingFaction) {
        Bukkit.getPlayer(uuid).teleport(defendingFaction.getFWorld().raiderTeleportLocation());
    }

    public Tier getTier() {
        return this.tier;
    }

    @EventHandler
    public void raidStart(RaidStartEvent event) {
        if (event.isCancelled()) return;
        // add the raid to the raid manager
        FRaidManager.addRaid(this);
        // start the countdown
        start();
        // teleport all online players to the factions world
        for (UUID uuid : event.getRaidingFaction().getOnlinePlayers()) {
            teleportToRaid(uuid, event.getDefendingFaction());
        }
        // add all offline players to the list so they can tp to the raid
        this.offlineRaiders.addAll(event.getRaidingFaction().getOfflinePlayers());
        // broadcast that the raid has started
        BroadcastType.RAID_START.broadcast(event.getDefendingFaction().getName(), event.getRaidingFaction().getName(), event.getTier().toString());
    }

    @EventHandler
    public void raidComplete(RaidCompletionEvent event) {
        if (event.isCancelled()) return;
        // remove the active raid
        FRaidManager.removeActiveRaid(this);
        // teleport all of the raiders back to the spawn location
        for (Player player : event.getDefendingFaction().getFWorld().getPlayers()) {
            FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
            if (fPlayer.isInHomeWorld()) continue;
            fPlayer.getPlayer().teleport(Bukkit.getWorld(FileManager.get("config").getString("main-world-name")).getSpawnLocation());
        }
        // reset the raid data for both factions
        resetRaidData();
        // set defending faction on raid cooldown
        FRaidManager.addFactionOnRaidCooldown(event.getDefendingFaction().getId());
        // broadcast that the raid is complete
        BroadcastType.RAID_COMPLETE.broadcast(event.getDefendingFaction().getName(), event.getRaidingFaction().getName(), event.getTier().toString());
    }
}
