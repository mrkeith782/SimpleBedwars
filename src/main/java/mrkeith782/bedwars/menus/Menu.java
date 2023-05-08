package mrkeith782.bedwars.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Menu {
    /**
     * Used in order to generate the menu. The menu should then be registered with the MenuManager.
     */
    void createMenu();

    /**
     * Used to handle inventory clicks while the player is in this specific menu.
     * @param e
     */
    void handleClick(InventoryClickEvent e);
    String getMenuID();
    String getMenuName();
}
