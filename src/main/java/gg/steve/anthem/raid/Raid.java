package gg.steve.anthem.raid;

import gg.steve.anthem.core.Faction;

public class Raid {
    private Faction defendingFaction;
    private Faction raidingFaction;
    private Tier tier;

    public Raid(Faction defendingFaction, Faction raidingFaction, Tier tier) {
        this.defendingFaction = defendingFaction;
        this.raidingFaction = raidingFaction;
        this.tier = tier;
    }
}
