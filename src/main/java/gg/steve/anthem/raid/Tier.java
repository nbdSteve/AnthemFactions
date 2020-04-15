package gg.steve.anthem.raid;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.wealth.AsyncWealthCalculation;

import java.util.List;

public enum Tier {
    TIER_1(FileManager.get("raid-config").getInt("tier-1.cost"), FileManager.get("raid-config").getInt("tier-1.start"), FileManager.get("raid-config").getInt("tier-1.finish")),
    TIER_2(FileManager.get("raid-config").getInt("tier-2.cost"), FileManager.get("raid-config").getInt("tier-2.start"), FileManager.get("raid-config").getInt("tier-2.finish")),
    TIER_3(FileManager.get("raid-config").getInt("tier-3.cost"), FileManager.get("raid-config").getInt("tier-3.start"), FileManager.get("raid-config").getInt("tier-3.finish"));

    private int cost;
    private int start;
    private int finish;
    private List<Faction> factions;

    Tier(int cost, int start, int finish) {
        this.cost = cost;
        this.start = start;
        this.finish = finish;
        if (finish == 999) finish = AsyncWealthCalculation.getFactionsInWealthOrder().size() - 1;
        this.factions = AsyncWealthCalculation.getFactionsInWealthOrder().subList(start, finish);
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
}
