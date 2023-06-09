package mrkeith782.bedwars.menus;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.builders.ItemBuilder;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.util.InventoryUtil;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ShopMenu extends Menu {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final MenuManager mm = bedwars.getMm();

    public ShopMenu() {
        this.menuID = "SHOP_MENU";
        this.menuName = "Shop";
        this.createMenu();
    }

    /**
     * Override to custom set the ID
     * @param id Identifier for the npc
     */
    public ShopMenu(String id) {
        this.menuID = id;
        this.menuName = "Shop";
        this.createMenu();
    }

    @Override
    public void createMenu() {
        Map<Integer, ItemStack> layout = new HashMap<>();
        layout.put(0, getMainMenuItem());
        IntStream.range(9, 18).forEach(i -> layout.put(i, mm.getBlankItem()));
        layout.put(18, getWoolItem());

        this.inventory = createInventory(6, layout);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            return;
        }

        if (clickedItem.isSimilar(getWoolItem())) {
            if (!InventoryUtil.hasSpace(player, new ItemStack(Material.WHITE_WOOL), 16)) {
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

    private ItemStack getWoolItem() {
        ItemBuilder woolItem = new ItemBuilder(new ItemStack(Material.WHITE_WOOL));
        woolItem.setName("%%white%%White Wool %%gray%%x16");
        woolItem.setStackSize(16);
        List<String> woolLore = new ArrayList<>();
        woolLore.add("%%gold%%Cost: %%white%%4 Iron");
        woolItem.setLore(woolLore);
        return woolItem.getItem();
    }

    private ItemStack getMainMenuItem() {
        ItemBuilder mainMenuItem = new ItemBuilder(new ItemStack(Material.WHITE_WOOL));
        mainMenuItem.setName("%%yellow%%All Items");
        return mainMenuItem.getItem();
    }
}
