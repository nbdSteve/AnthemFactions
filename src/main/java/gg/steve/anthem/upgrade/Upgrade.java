package gg.steve.anthem.upgrade;

import gg.steve.anthem.core.Faction;

public class Upgrade {
    private int level;
    private UpgradeType type;
    private Faction faction;

    public Upgrade(UpgradeType type, Faction faction) {
        this.faction = faction;
        this.level = faction.getData().get().getInt("upgrades." + type.toString().toLowerCase());
        this.type = type;
    }

    public boolean alreadyMaxLevel() {
        return level >= type.getMaxLevel();
    }

    public void incrementLevel() {
        this.faction.getData().get().set("upgrades." + type.toString().toLowerCase(), this.level + 1);
        this.faction.getData().save();
        this.level++;
    }

    public int getLevel() {
        return this.level;
    }

    public int getNextLevelCost() {
        if (!alreadyMaxLevel()) return type.getCost(this.level + 1);
        return faction.getXp();
    }

    public Faction getFaction() {
        return this.faction;
    }

    public UpgradeType getType() {
        return this.type;
    }
}
