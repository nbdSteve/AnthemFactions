package gg.steve.anthem;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.managers.SetupManager;
import gg.steve.anthem.upgrade.fchest.FChest;
import gg.steve.anthem.upgrade.fchest.FChestManager;
import gg.steve.anthem.wealth.AsyncWealthCalculation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public final class AnthemFactions extends JavaPlugin {
    private static AnthemFactions instance;
    private static DecimalFormat numberFormat = new DecimalFormat("#,###.##");

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        SetupManager.setupFiles(new FileManager(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        FChestManager.saveFChests();
        Bukkit.getServer().getScheduler().cancelTasks(instance);
    }

    public static AnthemFactions get() {
        return instance;
    }

    public static DecimalFormat getNumberFormat() {
        return numberFormat;
    }
}
