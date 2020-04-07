package gg.steve.anthem;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.managers.SetupManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnthemFactions extends JavaPlugin {
    private static AnthemFactions instance;

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
    }

    public static AnthemFactions get() {
        return instance;
    }
}
