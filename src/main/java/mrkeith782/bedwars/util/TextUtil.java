package mrkeith782.bedwars.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class TextUtil {

    /**
     * Used to parse strings with easy to type colors into color strings.
     * @param string String to be parsed
     * @return TextComponent with valid color codes
     */
    public static String parseColoredString(String string) {
        string = string.replace("%%black%%", "§0");
        string = string.replace("%%dark_blue%%", "§1");
        string = string.replace("%%dark_green%%", "§2");
        string = string.replace("%%dark_aqua%%", "§3");
        string = string.replace("%%dark_red%%", "§4");
        string = string.replace("%%dark_purple%%", "§5");
        string = string.replace("%%gold%%", "§6");
        string = string.replace("%%gray%%", "§7");
        string = string.replace("%%dark_gray%%", "§8");
        string = string.replace("%%blue%%", "§9");
        string = string.replace("%%green%%", "§a");
        string = string.replace("%%aqua%%", "§b");
        string = string.replace("%%red%%", "§c");
        string = string.replace("%%light_purple%%", "§d");
        string = string.replace("%%yellow%%", "§e");
        string = string.replace("%%white%%", "§f");
        string = string.replace("%%obf%%", "§k");
        string = string.replace("%%obfuscated%%", "§k");
        string = string.replace("%%bold%%", "§l");
        string = string.replace("%%strike%%", "§m");
        string = string.replace("%%underline%%", "§n");
        string = string.replace("%%italic%%", "§o");
        string = string.replace("%%reset%%", "§r");
        return string;
    }

    /**
     * Used to add hover-over text to existing TextComponents.
     * @param string TextComponent to hover over
     * @param hoverString TextComponent that appears on hover
     * @return TextComponent with hover
     */
    public static TextComponent addHoverText(TextComponent string, TextComponent hoverString) {
        string.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverString.getText())));
        return string;
    }

    /**
     * Displays a string on the player's action bar.
     * @param player Player to display the string to.
     * @param string String to display. Does not parse color codes.
     */
    public static void displayActionBar(Player player, String string) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(string));
    }

    /**
     * Returns a nicely formatted string for time
     * @param sec Seconds
     * @return Formatted string
     */
    public static String formatPrettyTime(int sec) {
        return (sec / 60) + ":" + (sec % 60 < 10 ? "0" : "") + sec % 60;
    }
}
