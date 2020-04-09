package gg.steve.anthem.world;

import gg.steve.anthem.managers.FileManager;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FWorldGeneration {

    public static void generate(String name) {
        //create the world creator
        WorldCreator worldCreator = new WorldCreator("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + name);
        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("2;0;1");
        worldCreator.generateStructures(false);
        //load the configuration file for the world generation
        YamlConfiguration config = FileManager.get("config");
        String section = "faction-world-generation.";
        worldCreator.seed(config.getInt(section + "seed"));
        int plotSize = config.getInt(section + "plot-size");
        int factionArea = config.getInt(section + "faction-area");
        //create a new FactionWorld variable
        FWorld fWorld = new FWorld(worldCreator.createWorld(), plotSize, factionArea);
        for (int x = -(plotSize + 1); x <= plotSize; x++) {
            for (int z = -(plotSize + 1); z <= plotSize; z++) {
                fWorld.getBlockAt(x, 1, z).setType(Material.BEDROCK);
                if (x >= -(factionArea + 1) && x <= factionArea && z >= -(factionArea + 1) && z <= factionArea) {
                    fWorld.getBlockAt(x, 2, z).setType(Material.STONE);
                }
            }
        }
        setupWorldBorder(config, section, fWorld);
        setupSpawnLimits(config, section, fWorld);
        applyGameRules(config, section, fWorld);
        setSpawnLocation(config, section, fWorld);
        if (config.getBoolean(section + "set-time.enabled")) {
            fWorld.setTime(config.getInt(section + "set-time.time"));
        }
    }

    private static void setupWorldBorder(YamlConfiguration config, String section, FWorld fWorld) {
        section += "world-border.";
        if (!config.getBoolean(section + "enabled")) return;
        String[] center = config.getString(section + "center").split(":");
        fWorld.getWorldBorder().setCenter(Double.parseDouble(center[0]), Double.parseDouble(center[1]));
        fWorld.getWorldBorder().setWarningDistance(config.getInt(section + "warning-distance"));
        fWorld.getWorldBorder().setSize(config.getInt(section + "size"));
    }

    private static void setupSpawnLimits(YamlConfiguration config, String section, FWorld fWorld) {
        section += "spawn-limits.";
        fWorld.setMonsterSpawnLimit(config.getInt(section + "monster"));
        fWorld.setWaterAnimalSpawnLimit(config.getInt(section + "water"));
        fWorld.setAnimalSpawnLimit(config.getInt(section + "animal"));
        fWorld.setAmbientSpawnLimit(config.getInt(section + "ambient"));
    }

    private static void applyGameRules(YamlConfiguration config, String section, FWorld fWorld) {
        for (String rule : config.getStringList(section + "game-rules")) {
            String[] gameRule = rule.split(":");
            fWorld.setGameRuleValue(gameRule[0], gameRule[1]);
        }
    }

    private static void setSpawnLocation(YamlConfiguration config, String section, FWorld fWorld) {
        String[] location = config.getString(section + "spawn").split(":");
        fWorld.setSpawnLocation(Integer.parseInt(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]));
    }
}
