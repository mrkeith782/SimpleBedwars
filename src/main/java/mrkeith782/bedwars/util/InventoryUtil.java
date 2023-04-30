package mrkeith782.bedwars.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
    /**
     * Removes specific amount of materials from a player's inventory.
     * @param player Player to remove items from
     * @param material Material type
     * @param amount Amount of items to remove
     * @return True if successful, false if not
     */
    public static boolean removeIfExists(Player player, Material material, int amount) {
        if (!player.getInventory().containsAtLeast(new ItemStack(material), 4)) {
            return false;
        }

        for (ItemStack item : player.getInventory()) {
            if (item == null || item.getType() != material) {
                continue;
            } else if (item.getAmount() >= amount) { //player has enough or more than enough in this slot
                item.setAmount(item.getAmount() - amount);
                break;
            } else { //player has 1<x<ironCost iron in this inventory slot
                amount -= item.getAmount();
                item.setAmount(0);
            }
        }

        player.updateInventory();
        return true;
    }

    /**
     * Adds a specific amount of an item to the player's inventory.
     * @param player Player to give item to
     * @param material Material to give
     * @param amount Amount of material to give
     */
    public static void giveItem(Player player, Material material, int amount) {
        ItemStack item = new ItemStack(material, amount);
        player.getInventory().addItem(item);
    }

    /**
     * Checks if the player's inventory can hold a given item
     * @param player Player's inventory to check
     * @param material Material to check
     * @param amount Amount of material to check
     * @return True if the player can hold the item, false if not.
     */
    public static boolean checkForSpace(Player player, Material material, int amount) {
        int emptySpace = 0;
        for (ItemStack item : player.getInventory()) {
            if (item == null) { //should mean that the player has a slot open
                emptySpace += 64;
                continue;
            } else if (item.getType() == material) {
                emptySpace += 64 - item.getAmount();
            }
        }
        return emptySpace >= amount;
    }
}
