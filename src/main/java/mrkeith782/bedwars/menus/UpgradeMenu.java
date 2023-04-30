package mrkeith782.bedwars.menus;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class UpgradeMenu implements Menu{
    private final Bedwars bedwars = Bedwars.getInstance();
    private final MenuManager mm = bedwars.getMm();
    String menuID = "UPGRADE_MENU";
    String menuName = "Upgrades";

    @Override
    public void createMenu() {
        ItemStack blankPane = mm.getBlankItem();
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
        mm.registerMenu(menuName, menuID, 1, layout);
    }

    @Override
    public void openMenu(Player player) {
        mm.openMenu(menuID, player);
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);
        e.getWhoClicked().sendMessage("Clicked in the UPGRADE_MENU!");
    }

    @Override
    public String getMenuName() {
        return menuName;
    }

    @Override
    public String getMenuID() {
        return menuID;
    }
}
