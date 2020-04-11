package gg.steve.anthem.relation;

import gg.steve.anthem.managers.FileManager;

public enum RelationType {
    ALLY(FileManager.get("config").getInt("max-allies"), FileManager.get("config").getString("relation-color.ally")),
    NEUTRAL(1000, FileManager.get("config").getString("relation-color.neutral")),
    ENEMY(FileManager.get("config").getInt("max-enemies"), FileManager.get("config").getString("relation-color.enemy")),
    FACTION(1, FileManager.get("config").getString("relation-color.faction")),
    WILDERNESS(1, FileManager.get("config").getString("relation-color.wilderness"));

    private final int maxAmount;
    private final String prefix;

    RelationType(int maxAmount, String prefix) {
        this.maxAmount = maxAmount;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getMaxAmount() {
        return maxAmount;
    }
}
