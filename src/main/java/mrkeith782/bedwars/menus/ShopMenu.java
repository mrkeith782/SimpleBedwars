package mrkeith782.bedwars.menus;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopMenu implements Menu {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final MenuManager mm = bedwars.getMm();
    String menuID = "SHOP_MENU";
    String menuName = "Shop";

    @Override
    public void createMenu() {
        Map<Integer, ItemStack> layout = new HashMap<>();
        layout.put(4, getWoolItem());

        mm.registerMenu(menuName, menuID, 1, layout);
        mm.fillWithBlanks(menuID);
    }

    @Override
    public void openMenu(Player player) {
        mm.openMenu(menuID, player);
    }

    @Override
    public String getMenuID() {
        return menuID;
    }

    private ItemStack getWoolItem() {
        ItemUtil woolItem = new ItemUtil(new ItemStack(Material.WHITE_WOOL));
        woolItem.setName("%%white%%White Wool %%gray%%x16");
        woolItem.setStackSize(16);
        List<String> woolLore = new ArrayList<>();
        woolLore.add("%%green%%Cost: %%white%%4 Iron");
        woolItem.setLore(woolLore);
        woolItem.addNBTData("item", "WOOL_16");
        return woolItem.getItem();
    }
}
