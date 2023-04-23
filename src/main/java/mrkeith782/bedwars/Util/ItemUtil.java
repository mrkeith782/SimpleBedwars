package mrkeith782.bedwars.Util;

import mrkeith782.bedwars.Bedwars;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    private final TextUtil textUtil = new TextUtil();
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

    public void setLore(List<String> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        List<String> coloredLore = new ArrayList<>();
        for(String string : lore) {
            coloredLore.add(textUtil.parseColoredString(string));
        }

        itemMeta.setLore(coloredLore);
    }

    public void addNBTData(String id, int value) {

    }
}
