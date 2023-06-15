package mrkeith782.bedwars.menus;

import lombok.Getter;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class Menu {
    @Getter
    String menuID;
    @Getter
    String menuName;
    @Getter
    Inventory inventory;

    /**
     * Used in order to generate the menu. This should set the inventory.
     */
    public abstract void createMenu();

    /**
     * Used to handle inventory clicks while the player is in this specific menu.
     *
     * @param event InventoryClickEvent
     */
    public abstract void handleClick(InventoryClickEvent event);

    /**
     * Creates an inventory with the set contents.
     *
     * @param rows How many rows the inventory has. Cannot be 0.
     * @param contents Item contents of the menu, mapped to their location.
     *
     * @return The created inventory, or null if it could not be created.
     */
    @Nullable
    public Inventory createInventory(int rows, Map<Integer, ItemStack> contents) {
        if (rows == 0 || rows > 6) {
            return null;
        }

        Inventory inv = Bukkit.createInventory(null, rows * 9, TextUtil.parseColoredString(this.menuName));
        contents.forEach(inv::setItem);

        return inv;
    }
}
