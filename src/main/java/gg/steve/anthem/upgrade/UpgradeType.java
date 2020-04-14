package gg.steve.anthem.upgrade;

import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum UpgradeType {
    RAIDING(FileManager.get("upgrade-config").getInt("raiding.max-level"), "raiding"),
    FARMING(FileManager.get("upgrade-config").getInt("farming.max-level"), "farming"),
    WORLD(FileManager.get("upgrade-config").getInt("world.max-level"), "world");

    private final int maxLevel;
    private final String path;
    private final List<Integer> levelCosts;

    UpgradeType(int maxLevel, String path) {
        this.maxLevel = maxLevel;
        this.path = path;
        this.levelCosts = new ArrayList<>();
        for (int i = 1; i <= this.maxLevel; i++) {
            this.levelCosts.add(FileManager.get("upgrade-config").getInt(this.path + ".level-" + i + ".cost"));
        }
    }

    public int getCost(int level) {
        return this.levelCosts.get(level - 1);
    }

    public void onClick(Upgrade upgrade, FPlayer fPlayer) {
        if (upgrade.alreadyMaxLevel()) {
            MessageType.UPGRADE_ALREADY_MAX.message(fPlayer);
            fPlayer.getPlayer().closeInventory();
            return;
        }
        if (upgrade.getFaction().getXp() < upgrade.getNextLevelCost()) {
            CommandDebug.INSUFFICIENT_XP.message(fPlayer);
            fPlayer.getPlayer().closeInventory();
            return;
        }
        upgrade.getFaction().withdrawXp(upgrade.getNextLevelCost());
        upgrade.incrementLevel();
        if (upgrade.getType().equals(UpgradeType.FARMING) && upgrade.getLevel() == 1) {
            fPlayer.getFWorld().setGameRuleValue("randomTickSpeed", "10");
        }
        fPlayer.getFaction().messageAllOnlinePlayers(MessageType.UPGRADE_SUCCESS, upgrade.getType().toString(), String.valueOf(upgrade.getLevel()), String.valueOf(upgrade.getType().getMaxLevel()), fPlayer.getName());
        for (HumanEntity player : fPlayer.getPlayer().getInventory().getViewers()) {
            player.closeInventory();
            fPlayer.getFaction().getUpgradeGui().refresh();
            fPlayer.getFaction().openUpgradeGui(FPlayerManager.getFPlayer(player.getUniqueId()));
        }
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}