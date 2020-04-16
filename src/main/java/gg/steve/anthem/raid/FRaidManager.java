package gg.steve.anthem.raid;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.raid.gui.FRaidGui;
import org.bukkit.Bukkit;

import java.util.*;

public class FRaidManager {
    private static Map<UUID, FRaid> activeFRaids;
    private static List<UUID> factionsOnRaidCooldown;
    private static FRaidGui fRaidGui;

    public static void init() {
        fRaidGui = new FRaidGui();
        activeFRaids = new HashMap<>();
        loadActiveRaids();
        Bukkit.getScheduler().runTaskTimerAsynchronously(AnthemFactions.get(), () -> {
            if (factionsOnRaidCooldown == null || factionsOnRaidCooldown.isEmpty()) return;
            try {
                for (UUID uuid : factionsOnRaidCooldown) {
                    Faction faction = FactionManager.getFaction(uuid);
                    if (faction.getId().equals(FactionManager.getWildernessId())) continue;
                    faction.setRaidCooldown(faction.getRaidCooldown() - 1);
                    faction.saveRaidCooldown();
                    if (faction.getRaidCooldown() == 0) {
                        removeFactionOnRaidCooldown(uuid);
                    }
                }
            } catch (ConcurrentModificationException e) {
                // all good
            }
        }, 0L, 20L);
    }

    public static void loadActiveRaids() {
        for (Faction faction : FactionManager.getFactionsAsList()) {
            if (faction.isOnRaidCooldown()) {
                FRaidManager.addFactionOnRaidCooldown(faction.getId());
            } else if (faction.isRaidActive()) {
                if (activeFRaids.containsKey(UUID.fromString(faction.getData().get().getString("raid.active-raid.UUID")))) {
                    faction.setfRaid(activeFRaids.get(UUID.fromString(faction.getData().get().getString("raid.active-raid.UUID"))));
                    continue;
                }
                faction.setfRaid(new FRaid(FactionManager.getFaction(UUID.fromString(faction.getData().get().getString("raid.active-raid.defending-faction"))),
                        FactionManager.getFaction(UUID.fromString(faction.getData().get().getString("raid.active-raid.raiding-faction"))),
                        Tier.valueOf(faction.getData().get().getString("raid.active-raid.tier")),
                        faction.getData().get().getInt("raid.active-raid.time-remaining"),
                        UUID.fromString(faction.getData().get().getString("raid.active-raid.UUID")),
                        faction.getData().get().getStringList("raid.active-raid.raiders")));
            }
        }
    }

    public static void addRaid(FRaid fRaid) {
        if (activeFRaids == null) activeFRaids = new HashMap<>();
        if (activeFRaids.containsKey(fRaid.getId())) return;
        activeFRaids.put(fRaid.getId(), fRaid);
    }

    public static void saveActiveRaids() {
        if (activeFRaids == null || activeFRaids.isEmpty()) return;
        for (FRaid FRaid : activeFRaids.values()) {
            FRaid.saveToFile();
        }
    }

    public static void removeActiveRaid(FRaid fRaid) {
        if (activeFRaids == null || activeFRaids.isEmpty()) return;
        activeFRaids.remove(fRaid.getId());
    }

    public static void addFactionOnRaidCooldown(UUID uuid) {
        if (factionsOnRaidCooldown == null) factionsOnRaidCooldown = new ArrayList<>();
        if (factionsOnRaidCooldown.contains(uuid)) return;
        factionsOnRaidCooldown.add(uuid);
    }

    public static void removeFactionOnRaidCooldown(UUID uuid) {
        if (factionsOnRaidCooldown == null || factionsOnRaidCooldown.isEmpty()) return;
        if (factionsOnRaidCooldown.contains(uuid)) {
            factionsOnRaidCooldown.remove(uuid);
        }
    }

    public static FRaidGui getfRaidGui() {
        return fRaidGui;
    }
}
