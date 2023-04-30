package mrkeith782.bedwars.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface Menu {
    void createMenu();
    void openMenu(Player player);
    void handleClick(InventoryClickEvent e);
    String getMenuID();
    String getMenuName();
}
