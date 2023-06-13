package mrkeith782.bedwars.menus;

import mrkeith782.bedwars.Bedwars;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class UpgradeMenu extends Menu {

    public UpgradeMenu() {
        this.menuID = "UPGRADE_MENU";
        this.menuName = "Upgrades";
        this.createMenu();
    }

    @Override
    public void createMenu() {
        ItemStack blankPane = Bedwars.getInstance().getBedwarsGame().getMenuManager().getBlankItem();
        Map<Integer, ItemStack> layout = new HashMap<>();
        layout.put(0, blankPane);
        layout.put(1, blankPane);
        layout.put(2, blankPane);
        layout.put(3, blankPane);
        layout.put(4, blankPane);
        layout.put(5, blankPane);
        layout.put(6, blankPane);
        layout.put(7, blankPane);
        layout.put(8, blankPane);
        this.inventory = createInventory(1, layout);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        event.getWhoClicked().sendMessage("Clicked in the UPGRADE_MENU!");
    }
}
