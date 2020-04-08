package gg.steve.anthem.core;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Class that handles creating and loading faction data files
 */
public class FactionDataFileUtil {
    //Store the file name string
    private String fileName;
    //Store the player file
    private File file;
    //Store the yaml config
    private YamlConfiguration config;

    public FactionDataFileUtil(String fileName, String factionName) {
        //Set instance variable
        this.fileName = fileName;
        //Get the player file
        file = new File(AnthemFactions.get().getDataFolder(), "faction-data" + File.separator + fileName + ".yml");
        //Load the configuration for the file
        config = YamlConfiguration.loadConfiguration(file);
        //If the file doesn't exist then set the defaults
        if (!file.exists()) {
            setupFactionFileDefaults(config, factionName, fileName);
        }
        save();
    }

    public FactionDataFileUtil(String fileName) {
        //Set instance variable
        this.fileName = fileName;
        //Get the player file
        file = new File(AnthemFactions.get().getDataFolder(), "faction-data" + File.separator + fileName + ".yml");
        //Load the configuration for the file
        config = YamlConfiguration.loadConfiguration(file);
        //If this method is being called then we know that the file exists
        save();
    }

    private void setupFactionFileDefaults(YamlConfiguration config, String factionName, String id) {
        //Set defaults for the information about the players tiers and currency
        config.set("id", id);
        config.set("name", factionName);
        config.createSection("faction-members");
        config.createSection("home-location");
        String[] fWorldSpawn = FileManager.get("config").getString("faction-world-generation.spawn").split(":");
        config.set("home-location.x", Integer.parseInt(fWorldSpawn[0]));
        config.set("home-location.y", Integer.parseInt(fWorldSpawn[1]));
        config.set("home-location.z", Integer.parseInt(fWorldSpawn[2]));
        config.set("wealth", 0);
        //Send a nice message
        LogUtil.info("Successfully created a new faction data file: " + fileName + ", actively creating / setting defaults.");
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            LogUtil.warning("Critical error saving the file: " + fileName + ", please contact nbdSteve#0583 on discord.");
        }
        reload();
    }

    public void reload() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            LogUtil.warning("Critical error loading the file: " + fileName + ", please contact nbdSteve#0583 on discord.");
        }
    }

    public void delete() {
        file.delete();
    }

    public YamlConfiguration get() {
        return config;
    }
}
