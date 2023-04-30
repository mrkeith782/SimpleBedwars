package mrkeith782.bedwars.menus;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.util.InventoryUtil;
import mrkeith782.bedwars.util.ItemUtil;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (Objects.equals(clickedItem, getWoolItem())) {
            if (!InventoryUtil.checkForSpace(player, Material.WHITE_WOOL, 16)) {
                player.sendMessage(TextUtil.parseColoredString("%%red%%You don't have the required space to hold this!"));
                //todo: play failure sound
                return;
            }
            if (InventoryUtil.removeIfExists(player, Material.IRON_INGOT, 4)) {
                InventoryUtil.giveItem(player, Material.WHITE_WOOL, 16);
                //todo: play success sound
                return;
            }
            player.sendMessage(TextUtil.parseColoredString("%%red%%You don't have the required materials to purchase this!"));
            //todo: play failure sound
        }
    }

    @Override
    public String getMenuName() {
        return menuName;
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
        return woolItem.getItem();
    }
}
