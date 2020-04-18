package gg.steve.anthem.wealth;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.raid.Tier;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class that calculates wealth for all of the factions on the server.
 * This task is async since it does not depend on the main thread
 */
public class AsyncWealthCalculation {

    /**
     * Void method called on start up to initialise the wealth calculation task
     */
    public static void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(AnthemFactions.get(), () -> {
            LogUtil.info("Calculating wealth for all factions...");
            for (UUID uuid : FactionManager.getFactions()) {
                Faction faction = FactionManager.getFaction(uuid);
                if (faction.getId().equals(FactionManager.getWildernessId())) continue;
                double wealth = 0;
                List<Block> blocks = faction.getFWorld().getFactionArea();
                for (Block block : blocks) {
                    for (String blockType : FileManager.get("worth").getKeys(false)) {
                        if (!block.getType().toString().equalsIgnoreCase(blockType)) continue;
                        // if the block is a mob spawner need to check the block data rather than byte
                        if (blockType.equalsIgnoreCase("mob_spawner")) {
                            if (((CreatureSpawner) block.getState()).getSpawnedType().getTypeId()
                                    != FileManager.get("worth").getInt(blockType + ".data")) continue;
                        } else {
                            if (block.getData() != FileManager.get("worth").getInt(blockType + ".data"))
                                continue;
                        }
                        wealth += FileManager.get("worth").getDouble(blockType + ".value");
                    }
                }
                faction.setWealth(wealth);
            }
            LogUtil.info("Wealth calculation complete, all faction top data is up to date.");
            Tier.refreshFactionsForTiers();
        }, 0L, FileManager.get("config").getLong("wealth-calculation-delay") * 20);
    }

    /**
     * Returns a list of factions based on wealth, most wealthy -> least wealthy
     *
     * @return List<Faction> Factions in wealth order
     */
    public static List<Faction> getFactionsInWealthOrder() {
        List<Faction> wealth = new ArrayList<>();
        for (UUID uuid : FactionManager.getFactions()) {
            if (uuid.equals(FactionManager.getWildernessId())) continue;
            wealth.add(FactionManager.getFaction(uuid));
        }
        // use wealth comparator to sort factions
        wealth.sort(new WealthComparator());
        return wealth;
    }
}