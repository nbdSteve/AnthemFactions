package gg.steve.anthem.core;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Class that handles creating and loading player files
 */
public class FactionDataFileUtil {
    //Store the file name string
    private String fileName;
    //Store the player file
    private File file;
    //Store the yaml config
    private YamlConfiguration config;

    public FactionDataFileUtil(String fileName, UUID owner) {
        //Set instance variable
        this.fileName = fileName;
        //Get the player file
        file = new File(AnthemFactions.get().getDataFolder(), "faction-data" + File.separator + fileName + ".yml");
        //Load the configuration for the file
        config = YamlConfiguration.loadConfiguration(file);
        //If the file doesn't exist then set the defaults
        if (!file.exists()) {
            setupPlayerFileDefaults(config, fileName, owner);
        }
        save();
    }

    private void setupPlayerFileDefaults(YamlConfiguration config, String name, UUID owner) {
        //Set defaults for the information about the players tiers and currency
        config.set("name", name);
        config.set("owner-uuid", owner.toString());
        config.createSection("moderators");
        config.createSection("recruits");
        config.set("wealth", 0);
        //Send a nice message
        LogUtil.info("Successfully created a new faction data file: " + fileName + ", actively creating / setting defaults.");
    }
//
//    private void setupChallengeWeeks() {
//        for (ChallengeWeek challengeWeek : WeeklyChallengeManager.weeks.values()) {
//            if (challengeWeek.isUnlocked()) {
//                for (Challenge challenge : challengeWeek.challenges) {
//                    if (config.get("pass-challenges.week-" + challengeWeek.getWeek() + "." + challenge.getId()) == null) {
//                        config.set("pass-challenges.week-" + challengeWeek.getWeek() + "." + challenge.getId(), 0.0);
//                    }
//                }
//            }
//        }
//    }

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
