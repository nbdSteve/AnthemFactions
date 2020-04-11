package gg.steve.anthem.managers;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.chat.PlayerChatListener;
import gg.steve.anthem.cmd.FactionsCmd;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.player.PlayerEventListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;

/**
 * Class that handles setting up the plugin on start
 */
public class SetupManager {

    private SetupManager() throws IllegalAccessException {
        throw new IllegalAccessException("Manager class cannot be instantiated.");
    }

    /**
     * Loads the files into the file manager
     *
     * @param fileManager FileManager, the plugins file manager
     */
    public static void setupFiles(FileManager fileManager) {
        // general files
        fileManager.add("config", "anthem-factions.yml");
        fileManager.add("lang", "lang.yml");
        fileManager.add("permissions", "permissions.yml");
        fileManager.add("cooldowns", "cooldowns.yml");
        // load lang files
        fileManager.add("relational", "lang" + File.separator + "relational.yml");
        fileManager.add("faction", "lang" + File.separator + "faction.yml");
        // load all of the factions for the server
        FactionManager.init();
        FPlayerManager.init();
        CooldownManager.init();
        DelayManager.init();
    }

    public static void registerCommands(AnthemFactions instance) {
        instance.getCommand("f").setExecutor(new FactionsCmd());
    }

    /**
     * Register all of the events for the plugin
     *
     * @param instance Plugin, the main plugin instance
     */
    public static void registerEvents(Plugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new FPlayerManager(), instance);
        pm.registerEvents(new PlayerEventListener(), instance);
        pm.registerEvents(new DelayManager(), instance);
        pm.registerEvents(new PlayerChatListener(), instance);
    }
}
