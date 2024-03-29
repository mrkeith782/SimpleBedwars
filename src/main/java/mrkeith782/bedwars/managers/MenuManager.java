package mrkeith782.bedwars.managers;

import lombok.Getter;
import mrkeith782.bedwars.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuManager {
    private final Map<String, Inventory> inventories = new HashMap<>();
    @Getter
    private final List<Menu> menus = new ArrayList<>();
    private ItemStack blankItem;

    /**
     * Registers a menu from a class that implements Menu
     *
     * @param menu Specific menu to register in the MM
     */
    public void registerMenu(Menu menu) {
        menus.add(menu);
        inventories.put(menu.getMenuID(), menu.getInventory());
    }

    /**
     * Returns a stored inventory in the MenuManager
     *
     * @param id ID of the stored menu
     *
     * @return Requested menu. Can be null.
     */
    @Nullable
    public Inventory getMenuByID(String id) {
        return inventories.get(id);
    }

    /**
     * Modifies a stored menu.
     *
     * @param id   ID of the menu to edit.
     * @param slot Slot number to place the new ItemStack in.
     * @param item Item to put into the Slot number.
     *
     * @return True if we're able to modify the menu, false if not
     */
    public boolean modifyMenu(String id, int slot, ItemStack item) {
        Inventory inventory = getMenuByID(id);
        if (inventory == null) {
            return false;
        }

        inventory.setItem(slot, item);
        inventories.replace(id, inventory);
        return true;
    }

    /**
     * Opens a menu for the player
     *
     * @param id     ID of the menu
     * @param player Player to display the menu to
     */
    public void openMenu(String id, Player player) {
        player.closeInventory();
        Inventory inventory = getMenuByID(id);
        if (inventory == null) {
            return;
        }

        player.openInventory(inventory);
    }

    /**
     * Creates a blank ItemStack
     *
     * @return Blank BLACK_STAINED_GLASS_PANE.
     */
    @Nullable
    public ItemStack getBlankItem() {
        if (blankItem != null) { // Early break so we aren't constantly creating new objects
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
     *
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

    /**
     * Removes all menus that are actively registered with the menumanager.
     */
    public void removeAllMenus() {
        for (Menu menu : this.menus) {
            this.menus.remove(menu);
        }
    }
}
