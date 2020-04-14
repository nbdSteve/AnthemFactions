package gg.steve.anthem.upgrade.crop;

import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.upgrade.UpgradeType;
import gg.steve.anthem.utils.XpUtil;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CropListener implements Listener {
    private static Map<String, Integer> cropXp;
    private static Map<Block, Cooldown> plantsOnXpDelay;

    public static void loadCropConfig() {
        cropXp = new HashMap<>();
        plantsOnXpDelay = new HashMap<>();
        for (String crop : FileManager.get("crop-config").getKeys(false)) {
            cropXp.put(crop.toUpperCase(), FileManager.get("crop-config").getInt(crop));
        }
    }

    @EventHandler
    public void sugarCanePlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isInHomeWorld() && fPlayer.getFWorld().inFactionLand(event.getBlock().getLocation())) {
            switch (event.getBlock().getType().toString()) {
                case "SUGAR_CANE_BLOCK":
                case "CACTUS":
                case "SUGAR_CANE":
                    plantsOnXpDelay.put(event.getBlock(), new Cooldown(CooldownType.PLANT_XP_DELAY));
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void breakCropEvent(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isInHomeWorld() && fPlayer.getFWorld().inFactionLand(event.getBlock().getLocation())) {
            if (!cropXp.containsKey(event.getBlock().getType().toString())) return;
            switch (event.getBlock().getType().toString()) {
                case "SUGAR_CANE_BLOCK":
                case "CACTUS":
                case "SUGAR_CANE":
                    if (fPlayer.getFaction().getUpgrade(UpgradeType.FARMING).getLevel() <= 2) return;
                    if (plantsOnXpDelay.containsKey(event.getBlock())) {
                        if (plantsOnXpDelay.get(event.getBlock()).isActive()) {
                            plantsOnXpDelay.remove(event.getBlock());
                            return;
                        }
                        plantsOnXpDelay.remove(event.getBlock());
                    }
                    XpUtil.setTotalExperience(fPlayer,
                            XpUtil.getTotalExperience(fPlayer) + (cropXp.get(event.getBlock().getType().toString())
                                    * getPlantHeight(event, fPlayer, event.getBlock().getType().toString())));
                    break;
                default:
                    if (event.getBlock().getData() != 7) return;
                    if (fPlayer.getFaction().getUpgrade(UpgradeType.FARMING).getLevel() <= 1) return;
                    for (ItemStack item : event.getBlock().getDrops()) {
                        fPlayer.getFWorld().dropItemNaturally(event.getBlock().getLocation(), item);
                    }
                    if (fPlayer.getFaction().getUpgrade(UpgradeType.FARMING).getLevel() <= 2) return;
                    XpUtil.setTotalExperience(fPlayer, XpUtil.getTotalExperience(fPlayer) + cropXp.get(event.getBlock().getType().toString()));
                    break;
            }
        }
    }

    public int getPlantHeight(BlockBreakEvent event, FPlayer fPlayer, String blockType) {
        int y = event.getBlock().getY();
        while (fPlayer.getWorld().getBlockAt(event.getBlock().getX(), y, event.getBlock().getZ()).
                getType().toString().equals(blockType) && y < 256) {
            y++;
        }
        return y - event.getBlock().getY();
    }
}