package gg.steve.anthem.wealth;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.*;

public class AsyncWealthCalculation {

    public static void init() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(AnthemFactions.get(), () -> {
            LogUtil.info("Calculating wealth for all factions...");
            for (UUID uuid : FactionManager.getFactions()) {
                Faction faction = FactionManager.getFaction(uuid);
                if (faction.getId().equals(FactionManager.getWildernessId())) continue;
                double wealth = 0;
                List<Block> blocks = faction.getFWorld().getFactionArea();
                for (Block block : blocks) {
                    if (FileManager.get("worth").getConfigurationSection(block.getType().toString().toLowerCase()) == null)
                        continue;
                    if (block.getData() == (FileManager.get("worth").getInt(block.getType().toString().toLowerCase() + ".data"))) {
                        wealth += (FileManager.get("worth").getDouble(block.getType().toString().toLowerCase() + ".value"));
                    }
                }
                faction.setWealth(wealth);
            }
            LogUtil.info("Wealth calculation complete, all faction top data is up to date.");
        }, 0L, FileManager.get("config").getLong("wealth-calculation-delay") * 20);
    }

    public static List<Faction> getFactionsInWealthOrder() {
        List<Faction> wealth = new ArrayList<>();
        for (UUID uuid : FactionManager.getFactions()) {
            wealth.add(FactionManager.getFaction(uuid));
        }
        wealth.sort(new WealthComparator());
        return wealth;
    }
}
