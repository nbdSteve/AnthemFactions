package gg.steve.anthem.managers;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.chat.PlayerChatListener;
import gg.steve.anthem.cmd.FactionsCmd;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.gui.GuiClickListener;
import gg.steve.anthem.player.DamageListener;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.player.PlayerEventListener;
import gg.steve.anthem.raid.FRaidEventListener;
import gg.steve.anthem.raid.FRaidManager;
import gg.steve.anthem.upgrade.crop.CropListener;
import gg.steve.anthem.upgrade.ffly.FFlyListener;
import gg.steve.anthem.wealth.AsyncWealthCalculation;
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
        fileManager.add("cooldowns", "cooldowns.yml");
        fileManager.add("worth", "worth.yml");
        // load lang files
        fileManager.add("relational", "lang" + File.separator + "relational.yml");
        fileManager.add("faction", "lang" + File.separator + "faction.yml");
        fileManager.add("debug", "lang" + File.separator + "command-debug.yml");
        fileManager.add("misc", "lang" + File.separator + "misc.yml");
        fileManager.add("broadcasts", "lang" + File.separator + "broadcasts.yml");
        fileManager.add("action-bars", "lang" + File.separator + "action-bars.yml");
        // load upgrade files
        fileManager.add("upgrade-config", "upgrades" + File.separator + "config.yml");
        fileManager.add("upgrade-gui", "upgrades" + File.separator + "gui.yml");
        fileManager.add("crop-config", "upgrades" + File.separator + "plant-xp.yml");
        // load permissions files
        fileManager.add("permissions", "permissions" + File.separator + "permissions.yml");
        fileManager.add("fperms-gui", "permissions" + File.separator + "fperms-gui.yml");
        fileManager.add("fperms-config", "permissions" + File.separator + "config.yml");
        fileManager.add("fperms-page-gui", "permissions" + File.separator + "page-gui.yml");
        // load raid files
        fileManager.add("raid-config", "raid" + File.separator + "config.yml");
        fileManager.add("raid-gui", "raid" + File.separator + "raid-gui.yml");
        fileManager.add("confirmation-gui", "raid" + File.separator + "confirmation-gui.yml");
        fileManager.add("TIER_1", "raid" + File.separator + "tier-1-gui.yml");
        fileManager.add("TIER_2", "raid" + File.separator + "tier-2-gui.yml");
        // disband util files
        fileManager.add("disbanded-players", "faction-data" + File.separator + "_disbanded-players.yml");
        // load all of the factions for the server
        FactionManager.init();
        FPlayerManager.init();
        CooldownManager.init();
        DelayManager.init();
        CropListener.loadCropConfig();
        AsyncWealthCalculation.init();
        FRaidManager.init();
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
        pm.registerEvents(new DamageListener(), instance);
        pm.registerEvents(new GuiClickListener(), instance);
        pm.registerEvents(new CropListener(), instance);
        pm.registerEvents(new FFlyListener(), instance);
        pm.registerEvents(new FRaidEventListener(), instance);
    }
}
