package gg.steve.anthem.create;

import gg.steve.anthem.managers.FileManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;

public class WorldGeneration {

    public static World generate(String name) {
        WorldCreator worldCreator = new WorldCreator("plugins" + File.separator + "AnthemFactions" + File.separator + "worlds" + File.separator + name);
        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("2;0;1");
        worldCreator.generateStructures(false);
        worldCreator.seed(FileManager.get("config").getInt("faction-world-generation.seed"));
        World world = worldCreator.createWorld();
        int plotSize = FileManager.get("config").getInt("faction-world-generation.plot-size");
        int factionArea = FileManager.get("config").getInt("faction-world-generation.faction-area");
        for (int x = -(plotSize - 1); x <= plotSize; x++) {
            for (int z = -(plotSize - 1); z <= plotSize; z++) {
                world.getBlockAt(x, 1, z).setType(Material.BEDROCK);
                if (x >= -(factionArea - 1) && x <= factionArea && z >= -(factionArea - 1) && z <= factionArea) {
                    world.getBlockAt(x, 2, z).setType(Material.STONE);
                }
            }
        }
        world.getWorldBorder().setCenter(0.0, 0.0);
        world.getWorldBorder().setWarningDistance(0);
        world.getWorldBorder().setSize(plotSize * 2 + 2);
        world.setWaterAnimalSpawnLimit(0);
        world.setAnimalSpawnLimit(0);
        world.setAmbientSpawnLimit(0);
        world.setMonsterSpawnLimit(0);
//        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
//        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setTime(6000);
//        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setSpawnLocation(0, 2, 0);
        return world;
    }
}
