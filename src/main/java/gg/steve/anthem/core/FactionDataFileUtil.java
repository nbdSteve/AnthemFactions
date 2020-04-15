package gg.steve.anthem.core;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        config.set("founded", new SimpleDateFormat(FileManager.get("config").getString("who-cmd.founded-format")).format(new Date(System.currentTimeMillis())));
        config.createSection("faction-members");
        config.createSection("home-location");
        String[] fWorldSpawn = FileManager.get("config").getString("faction-world-generation.spawn").split(":");
        config.set("home-location.x", Integer.parseInt(fWorldSpawn[0]));
        config.set("home-location.y", Integer.parseInt(fWorldSpawn[1]));
        config.set("home-location.z", Integer.parseInt(fWorldSpawn[2]));
        if (factionName.equalsIgnoreCase("wilderness")) {
            //load wilderness permissions
            config.createSection("permissions.wilderness");
            List<String> nodes = FileManager.get("config").getStringList("default-faction-permissions.wilderness");
            config.set("permissions.wilderness", nodes);
        } else {
            //load owner permissions
            config.createSection("permissions.owner");
            List<String> owner = FileManager.get("config").getStringList("default-faction-permissions.owner");
            config.set("permissions.owner", owner);
            //load co_owner permissions
            config.createSection("permissions.co_owner");
            List<String> coOwner = FileManager.get("config").getStringList("default-faction-permissions.co_owner");
            config.set("permissions.co_owner", coOwner);
            //load moderator permissions
            config.createSection("permissions.moderator");
            List<String> moderator = FileManager.get("config").getStringList("default-faction-permissions.moderator");
            config.set("permissions.moderator", moderator);
            //load member permissions
            config.createSection("permissions.member");
            List<String> member = FileManager.get("config").getStringList("default-faction-permissions.member");
            config.set("permissions.member", member);
        }
        config.createSection("relations.ally");
        config.createSection("relations.enemy");
        config.createSection("relations.outgoing-ally-requests");
        config.set("wealth", 0.0);
        config.set("xp-bank", 0);
        config.set("upgrades.raiding", 0);
        config.set("upgrades.farming", 0);
        config.set("upgrades.world", 0);
        config.createSection("f-chest");
        config.set("raid.cooldown", 0);
        config.set("raid.active-raid.active", false);
        config.set("raid.active-raid.faction", null);
        config.set("raid.active-raid.time-remaining", null);
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
