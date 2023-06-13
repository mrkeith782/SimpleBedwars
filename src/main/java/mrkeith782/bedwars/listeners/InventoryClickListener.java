package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.menus.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // We're only worried about clicks in the game
        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null) {
            return;
        }

        // We only care about the click if we're in a Menu, so let's check which one we're in, and pass the event accordingly
        for (Menu menu : game.getMenuManager().getMenus()) {
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