package mrkeith782.bedwars.Menus;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.Managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShopMenu implements Menu {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final MenuManager mm = bedwars.getMm();
    String menuID = "SHOP_MENU";

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
        mm.registerMenu("Shop", menuID, 1, layout);
    }

    @Override
    public void openMenu(Player player) {
        mm.openMenu(menuID, player);
    }

    @Override
    public String getMenuID() {
        return menuID;
    }
}
