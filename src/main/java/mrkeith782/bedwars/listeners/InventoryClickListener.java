package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.menus.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final MenuManager mm = bedwars.getMm();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for (Menu menu : mm.getMenus()) {
            String menuName = menu.getMenuName();
            if (menuName == null || e.getClickedInventory() == null) {
                continue;
            }
            if (e.getView().getTitle().equals(menuName)) {
                menu.handleClick(e);
                return;
            }
        }
    }
}
