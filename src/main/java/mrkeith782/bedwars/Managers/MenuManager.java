package mrkeith782.bedwars.Managers;

import mrkeith782.bedwars.Menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

public class MenuManager {
    private final Map<String, Inventory> inventories = new HashMap<>();
    private static ItemStack blankItem; //We only create it if we need it, and store it here.
    private final List<Menu> menus = new ArrayList<>();

    /**
     * Registers an inventory that can be used later.
     * @param name Name of the inventory. Supports color codes.
     * @param id ID of the inventory to access later.
     * @param contents Item contents of the menu, mapped to their location.
     */
    public void registerMenu(String name, String id, int rows, Map<Integer, ItemStack> contents) {
        if (rows == 0) return;
        Inventory inventory = Bukkit.createInventory(null, rows * 9, name);
        for (int key : contents.keySet()) {
            ItemStack item = contents.get(key);
            inventory.setItem(key, item);
        }
        inventories.put(id, inventory);
    }

    /**
     * Registers a menu from a class that implements Menu
     * @param menu
     */
    public void registerMenu(Menu menu) {
        menu.createMenu();
        menus.add(menu);
    }

    /**
     * Returns a stored inventory in the MenuManager
     * @param id ID of the stored menu
     * @return Requested menu. Can be null.
     */
    @Nullable
    public Inventory getMenuByID(String id) {
        return inventories.get(id);
    }

    /**
     * Modifies a stored menu.
     * @param id ID of the menu to edit.
     * @param slot Slot number to place the new ItemStack in.
     * @param item Item to put into the Slot number.
     * @return True if we're able to modify the menu, false if not
     */
    public boolean modifyMenu(String id, int slot, ItemStack item) {
        Inventory inventory = getMenuByID(id);
        if (inventory == null) return false;
        inventory.setItem(slot, item);
        inventories.replace(id, inventory);
        return true;
    }

    /**
     * Opens a menu for the player
     * @param id     ID of the menu
     * @param player
     */
    public void openMenu(String id, Player player) {
        player.closeInventory();
        Inventory inventory = getMenuByID(id);
        if (inventory == null) return;
        player.openInventory(inventory);
    }

    /**
     * Creates a blank ItemStack
     * @return Blank BLACK_STAINED_GLASS_PANE.
     */
    @Nullable
    public ItemStack getBlankItem() {
        if (blankItem != null) { //Early break so we aren't constantly creating new objects
            return blankItem;
        }

        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);

        blankItem = item;
        return blankItem;
    }

    /**
     * Fills the empty slots of the menu with blank panes.
     * @param id ID of the menu
     */
    public void fillWithBlanks(String id) {
        ItemStack blankPane = getBlankItem();
        if (blankPane == null) return;

        Inventory inventory = getMenuByID(id);
        if (inventory == null) return;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack mat = inventory.getItem(i);
            if (mat == null || mat.getType() == Material.AIR) {
                inventory.setItem(i, blankPane);
            }
        }
        inventories.replace(id, inventory);
    }

    public List<Menu> getMenus() {
        return menus;
    }
}
