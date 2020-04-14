package gg.steve.anthem.upgrade.fchest;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FChest implements Listener {
    private Inventory inventory;
    private Faction faction;
    private Map<Integer, ItemStack> items;

    public FChest(Faction faction, Map<Integer, ItemStack> items) {
        this.faction = faction;
        this.items = items;
        this.inventory = Bukkit.createInventory(null,
                FileManager.get("upgrade-config").getInt("f-chest.size"),
                ColorUtil.colorize(FileManager.get("upgrade-config").getString("f-chest.name").replace("{faction}", this.faction.getName())));
        setupItems();
    }

    public void setupItems() {
        if (this.items.isEmpty()) return;
        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot));
        }
    }

    public void open(FPlayer fPlayer) {
        fPlayer.getPlayer().openInventory(this.inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
