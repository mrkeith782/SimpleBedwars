package mrkeith782.bedwars.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class Menu {
    String menuID;
    String menuName;

    /**
     * Used in order to generate the menu. The menu should then be registered with the MenuManager.
     */
    public abstract void createMenu();

    /**
     * Used to handle inventory clicks while the player is in this specific menu.
     * @param e
     */
    public abstract void handleClick(InventoryClickEvent e);
    public String getMenuID() {
        return menuID;
    }
    public String getMenuName() {
        return menuName;
    }
}
