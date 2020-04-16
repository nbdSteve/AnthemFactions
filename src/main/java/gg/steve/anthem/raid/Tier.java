package gg.steve.anthem.raid;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.raid.gui.TierGui;
import gg.steve.anthem.wealth.AsyncWealthCalculation;

import java.util.List;

public enum Tier {
    TIER_1(FileManager.get("raid-config").getInt("tier-1.cost"),
            FileManager.get("raid-config").getInt("tier-1.start"),
            FileManager.get("raid-config").getInt("tier-1.finish"),
            FileManager.get("raid-config").getInt("tier-1.raid-length"),
            FileManager.get("raid-config").getInt("tier-1.raid-cooldown")),
    TIER_2(FileManager.get("raid-config").getInt("tier-2.cost"),
            FileManager.get("raid-config").getInt("tier-2.start"),
            FileManager.get("raid-config").getInt("tier-2.finish"),
            FileManager.get("raid-config").getInt("tier-2.raid-length"),
            FileManager.get("raid-config").getInt("tier-2.raid-cooldown")),
    TIER_3(FileManager.get("raid-config").getInt("tier-3.cost"),
            FileManager.get("raid-config").getInt("tier-3.start"),
            FileManager.get("raid-config").getInt("tier-3.finish"),
            FileManager.get("raid-config").getInt("tier-3.raid-length"),
            FileManager.get("raid-config").getInt("tier-3.raid-cooldown"));

    private int cost;
    private int start;
    private int finish;
    private int length;
    private int cooldown;
    private List<Faction> factions;
    private TierGui tierGui;

    Tier(int cost, int start, int finish, int length, int cooldown) {
        this.cost = cost;
        this.start = start;
        this.finish = finish;
        this.length = length;
        this.cooldown = cooldown;
        if (start > AsyncWealthCalculation.getFactionsInWealthOrder().size()) this.start = AsyncWealthCalculation.getFactionsInWealthOrder().size();
        if (finish > AsyncWealthCalculation.getFactionsInWealthOrder().size()) this.finish = AsyncWealthCalculation.getFactionsInWealthOrder().size();
        this.factions = AsyncWealthCalculation.getFactionsInWealthOrder().subList(this.start, this.finish);
    }

    public int getCost() {
        return cost;
    }

    public int getStart() {
        return start;
    }

    public int getFinish() {
        return finish;
    }

    public void setFactions(List<Faction> factions) {
        this.factions = factions;
    }

    public void refreshFactions() {
        this.factions = AsyncWealthCalculation.getFactionsInWealthOrder().subList(this.start, this.finish);
    }

    public boolean isTier3Factions() {
        return AsyncWealthCalculation.getFactionsInWealthOrder().size() > 54;
    }

    public Faction getRandomFaction() {
        int index = 54 + (int)(Math.random() * AsyncWealthCalculation.getFactionsInWealthOrder().size());
        return AsyncWealthCalculation.getFactionsInWealthOrder().get(index);
    }

    public List<Faction> getFactions() {
        return this.factions;
    }

    public static void refreshFactionsForTiers() {
        for (Tier tier : Tier.values()) {
            tier.refreshFactions();
        }
    }

    public static Tier getTier(Faction faction) {
        for (Tier tier : Tier.values()) {
            if (tier.getFactions().contains(faction)) return tier;
        }
        return null;
    }

    public int getDuration() {
        return length;
    }

    public int getCooldown() {
        return cooldown;
    }

    public TierGui getTierGui() {
        if (tierGui == null) this.tierGui = new TierGui(Tier.valueOf(name()));
        return this.tierGui;
    }
}
