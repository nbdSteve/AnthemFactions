package gg.steve.anthem.relation;

import gg.steve.anthem.managers.FileManager;

public enum RelationType {
    ALLY(FileManager.get("config").getInt("max-allies")),
    NEUTRAL(1000),
    ENEMY(FileManager.get("config").getInt("max-enemies"));

    private final int maxAmount;

    RelationType(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }
}
