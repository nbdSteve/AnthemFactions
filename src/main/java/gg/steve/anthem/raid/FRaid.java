package gg.steve.anthem.raid;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionDataFileUtil;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.raid.events.RaidCompletionEvent;
import gg.steve.anthem.raid.events.RaidCompletionType;
import gg.steve.anthem.raid.events.RaidStartEvent;
import gg.steve.anthem.upgrade.UpgradeType;
import gg.steve.anthem.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FRaid implements Listener {
    private Faction defendingFaction;
    private Faction raidingFaction;
    private Tier tier;
    private FPlayer starter;
    private BukkitTask task;
    private int remaining;
    private List<String> raiders;
    private UUID id;
    private boolean active;

    public FRaid(Faction defendingFaction, Faction raidingFaction, Tier tier, FPlayer starter) {
        this.defendingFaction = defendingFaction;
        this.raidingFaction = raidingFaction;
        this.tier = tier;
        this.starter = starter;
        this.remaining = tier.getDuration();
        this.raiders = new ArrayList<>();
        this.id = UUID.randomUUID();
        this.active = true;
        Bukkit.getPluginManager().callEvent(new RaidStartEvent(this, defendingFaction, raidingFaction, tier, starter));
    }

    public FRaid(Faction defendingFaction, Faction raidingFaction, Tier tier, int remaining, UUID id, List<String> raiders) {
        this.defendingFaction = defendingFaction;
        this.raidingFaction = raidingFaction;
        this.tier = tier;
        this.remaining = remaining;
        this.id = id;
        this.raiders = raiders;
        this.active = true;
        FRaidManager.addRaid(this);
        start();
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
                this.remaining -= 5;
                return;
            }
            Bukkit.getPluginManager().callEvent(new RaidCompletionEvent(this, defendingFaction, raidingFaction, tier, RaidCompletionType.TIMEOUT));
        }, 0L, 100L);
    }

    public boolean isActive() {
        return this.active;
    }

    public void saveToFile() {
        FactionDataFileUtil defendingData = this.defendingFaction.getData();
        defendingData.get().set("raid.active-raid.active", true);
        defendingData.get().set("raid.active-raid.UUID", this.id.toString());
        defendingData.get().set("raid.active-raid.raiding-faction", this.raidingFaction.getId().toString());
        defendingData.get().set("raid.active-raid.defending-faction", this.defendingFaction.getId().toString());
        defendingData.get().set("raid.active-raid.tier", this.tier.toString());
        defendingData.get().set("raid.active-raid.time-remaining", this.remaining);
        defendingData.save();
        FactionDataFileUtil raidingData = this.raidingFaction.getData();
        raidingData.get().set("raid.active-raid.active", true);
        raidingData.get().set("raid.active-raid.UUID", this.id.toString());
        raidingData.get().set("raid.active-raid.raiding-faction", this.raidingFaction.getId().toString());
        raidingData.get().set("raid.active-raid.defending-faction", this.defendingFaction.getId().toString());
        raidingData.get().set("raid.active-raid.tier", this.tier.toString());
        raidingData.get().set("raid.active-raid.time-remaining", this.remaining);
        raidingData.get().set("raid.active-raid.raiders", this.raiders);
        raidingData.save();
    }

    public void resetRaidData() {
        FactionDataFileUtil defendingData = this.defendingFaction.getData();
        defendingData.get().set("raid.active-raid.active", false);
        defendingData.get().set("raid.active-raid.UUID", "null");
        defendingData.get().set("raid.active-raid.raiding-faction", "null");
        defendingData.get().set("raid.active-raid.defending-faction", "null");
        defendingData.get().set("raid.active-raid.tier", "null");
        defendingData.get().set("raid.active-raid.time-remaining", 0);
        defendingData.get().set("raid.active-raid.raiders", new ArrayList<>());
        defendingData.save();
        FactionDataFileUtil raidingData = this.raidingFaction.getData();
        raidingData.get().set("raid.active-raid.active", false);
        raidingData.get().set("raid.active-raid.UUID", "null");
        raidingData.get().set("raid.active-raid.raiding-faction", "null");
        raidingData.get().set("raid.active-raid.defending-faction", "null");
        raidingData.get().set("raid.active-raid.tier", "null");
        raidingData.get().set("raid.active-raid.time-remaining", 0);
        raidingData.get().set("raid.active-raid.raiders", new ArrayList<>());
        raidingData.save();
    }

    public UUID getRaidingFaction() {
        return this.raidingFaction.getId();
    }

    public UUID getDefendingFaction() {
        return this.defendingFaction.getId();
    }

    public boolean isRaider(UUID uuid) {
        return this.raiders.contains(uuid.toString());
    }

    public void addRaider(String uuid) {
        this.raiders.add(uuid);
    }

    public List<String> getRaiders() {
        return raiders;
    }

    public void teleportToRaid(UUID uuid, Faction defendingFaction) {
        Player player = Bukkit.getPlayer(uuid);
        if (this.raidingFaction.getUpgrade(UpgradeType.RAIDING).getLevel() > 2) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        }
        player.teleport(defendingFaction.getFWorld().raiderTeleportLocation());
    }

    public Tier getTier() {
        return this.tier;
    }

    public int getRemaining() {
        return this.remaining;
    }

    public UUID getId() {
        return id;
    }

    public void cancelCounter() {
        this.task.cancel();
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
