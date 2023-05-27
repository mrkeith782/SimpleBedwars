package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.menus.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //We only care about the click if we're in a Menu, so let's check if we are.
        for (Menu menu : Bedwars.getInstance().getMm().getMenus()) {
            String menuName = menu.getMenuName();
            if (menuName == null || event.getClickedInventory() == null) {
                continue;
            }
            if (event.getView().getTitle().equals(menuName)) {
                menu.handleClick(event);
                return;
            }
        }
    }
}