package gg.steve.anthem.raid;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FRaidManager {
    private static List<FRaid> activeFRaids;
    private static List<UUID> factionsOnRaidCooldown;
    private static FRaidGui fRaidGui;

    public static void init() {
        fRaidGui = new FRaidGui();
        Bukkit.getScheduler().runTaskTimerAsynchronously(AnthemFactions.get(), () -> {
            if (factionsOnRaidCooldown == null || factionsOnRaidCooldown.isEmpty()) return;
            for (UUID uuid : factionsOnRaidCooldown) {
                Faction faction = FactionManager.getFaction(uuid);
                if (faction.getId().equals(FactionManager.getWildernessId())) continue;
                faction.setRaidCooldown(faction.getRaidCooldown() - 1);
                faction.saveRaidCooldown();
                if (faction.getRaidCooldown() == 0) {
                    removeFactionOnRaidCooldown(uuid);
                }
            }
        }, 0L, 20L);
    }

    public static void addRaid(FRaid fRaid) {
        if (activeFRaids == null) activeFRaids = new ArrayList<>();
        if (activeFRaids.contains(fRaid)) return;
        activeFRaids.add(fRaid);
    }

    public static void saveActiveRaids() {
        if (activeFRaids == null || activeFRaids.isEmpty()) return;
        for (FRaid FRaid : activeFRaids) {
            FRaid.saveToFile();
        }
    }

    public static void removeActiveRaid(FRaid fRaid) {
        if (activeFRaids == null || activeFRaids.isEmpty()) return;
        if (activeFRaids.contains(fRaid)) {
            activeFRaids.remove(fRaid);
        }
    }

    public static void addFactionOnRaidCooldown(UUID uuid) {
        if (factionsOnRaidCooldown == null) factionsOnRaidCooldown = new ArrayList<>();
        if (factionsOnRaidCooldown.contains(uuid)) return;
        factionsOnRaidCooldown.add(uuid);
    }

    public static void removeFactionOnRaidCooldown(UUID uuid) {
        if (factionsOnRaidCooldown == null || factionsOnRaidCooldown.isEmpty()) return;
        if (factionsOnRaidCooldown.contains(uuid)) {
            factionsOnRaidCooldown.add(uuid);
        }
    }

    public static FRaidGui getfRaidGui() {
        return fRaidGui;
    }
}
