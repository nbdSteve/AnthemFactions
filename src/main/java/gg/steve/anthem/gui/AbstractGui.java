package gg.steve.anthem.gui;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class AbstractGui {
    //Store the map of inventories that are created, static so it can be accessed and modified outside the class
    public static HashMap<UUID, AbstractGui> inventoriesByID = new HashMap<>();
    //Store the map of inventories that are open for players, static so it can be accessed and modified
    // outside the class
    public static HashMap<UUID, UUID> openInventories = new HashMap<>();

    //Store the id of the inventory
    private UUID inventoryID;
    //Store the inventory being created
    private Inventory inventory;
    //Store the map of actions to be called in the InventoryClickEvent
    private HashMap<Integer, inventoryClickActions> clickActions;

    /**
     * Constructor the create a new Gui
     */
    public AbstractGui(YamlConfiguration config, String type, Integer... size) {
        this.inventoryID = UUID.randomUUID();
        if (!type.equalsIgnoreCase("null")) {
            this.inventory = Bukkit.createInventory(null, InventoryType.valueOf(type), ColorUtil.colorize(config.getString("name")));
        } else {
            this.inventory = Bukkit.createInventory(null, size[0], ColorUtil.colorize(config.getString("name")));
        }
        this.clickActions = new HashMap<>();
        inventoriesByID.put(getInventoryID(), this);
    }

    public AbstractGui(YamlConfiguration config, String type, Role role, Integer... size) {
        this.inventoryID = UUID.randomUUID();
        if (!type.equalsIgnoreCase("null")) {
            this.inventory = Bukkit.createInventory(null, InventoryType.valueOf(type), ColorUtil.colorize(config.getString("name")));
        } else {
            this.inventory = Bukkit.createInventory(null, size[0], ColorUtil.colorize(config.getString("name").replace("{page}", role.toString())));
        }
        this.clickActions = new HashMap<>();
        inventoriesByID.put(getInventoryID(), this);
    }

    public AbstractGui(YamlConfiguration config, String type, Faction faction, Integer... size) {
        this.inventoryID = UUID.randomUUID();
        if (!type.equalsIgnoreCase("null")) {
            this.inventory = Bukkit.createInventory(null, InventoryType.valueOf(type), ColorUtil.colorize(config.getString("name")));
        } else {
            this.inventory = Bukkit.createInventory(null, size[0], ColorUtil.colorize(config.getString("name").replace("{faction}", faction.getName())));
        }
        this.clickActions = new HashMap<>();
        inventoriesByID.put(getInventoryID(), this);
    }

    /**
     * Get the inventory map
     *
     * @return
     */
    public static HashMap<UUID, AbstractGui> getInventoriesByID() {
        return inventoriesByID;
    }

    /**
     * Get the open inventories map
     *
     * @return
     */
    public static HashMap<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    /**
     * Will the the given item to the respective slot in the Gui
     *
     * @param guiSlot     Integer, slot in the Gui
     * @param item        ItemStack, the item to place there
     * @param clickAction the event called when the player clicks this item
     */
    public void setItemInSlot(int guiSlot, ItemStack item, inventoryClickActions clickAction) {
        inventory.setItem(guiSlot, item);
        if (clickAction != null) {
            clickActions.put(guiSlot, clickAction);
        }
    }

    /**
     * Void method that will open the inventory for the player
     *
     * @param player Player, the player to open the inventory for
     */
    public void open(Player player) {
        player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), getInventoryID());
    }

    /**
     * Void method to delete all of the open inventories
     */
    public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (openInventories.get(player.getUniqueId()).equals(getInventoryID())) {
                player.closeInventory();
            }
        }
        inventoriesByID.remove(getInventoryID());
    }

    /**
     * Getter for the inventory
     *
     * @return
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Getter for the inventory ID
     *
     * @return
     */
    public UUID getInventoryID() {
        return inventoryID;
    }

    /**
     * Getter for the inventory actions map
     *
     * @return
     */
    public HashMap<Integer, inventoryClickActions> getClickActions() {
        return clickActions;
    }

    /**
     * Interface to create a click action
     */
    public interface inventoryClickActions {
        void itemClick(Player player);
    }
}