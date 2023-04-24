package mrkeith782.bedwars.Util;

import mrkeith782.bedwars.Bedwars;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    private final TextUtil textUtil = new TextUtil();
    private final Bedwars bedwars = Bedwars.getInstance();
    private final ItemStack item;

    public ItemUtil(ItemStack item) {
        this.item = item;
    }

    /**
     * Set the name of an ItemStack with color code handling
     * @param string Name of object with supporting color codes
     */
    public void setName(String string) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        itemMeta.setDisplayName(textUtil.parseColoredString(string));
        item.setItemMeta(itemMeta);
    }

    /**
     * Set the lore of an ItemStack with color code handling
     * @param lore Lore of object, printed in List order.
     */
    public void setLore(List<String> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        List<String> coloredLore = new ArrayList<>();
        for(String string : lore) {
            coloredLore.add(textUtil.parseColoredString(string));
        }

        itemMeta.setLore(coloredLore);
        item.setItemMeta(itemMeta);
    }

    /**
     * Adds integer NBT data for the item.
     * @param id An ID to represent the data.
     * @param value A value that contains data.
     */
    public void addNBTData(String id, int value) {
        NamespacedKey key = new NamespacedKey(bedwars, id);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        item.setItemMeta(itemMeta);
    }

    /**
     * Adds string NBT data for the item.
     * @param id An ID to represent the data.
     * @param value A string that contains data.
     */
    public void addNBTData(String id, String value) {
        NamespacedKey key = new NamespacedKey(bedwars, id);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        item.setItemMeta(itemMeta);
    }

    public void setStackSize(int count) {
        item.setAmount(count); //I know this is a weird wrapper but seeing as I'm using it a lot, meh
    }

    public ItemStack getItem() {
        return item;
    }
}
