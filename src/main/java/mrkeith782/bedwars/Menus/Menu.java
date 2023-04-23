package mrkeith782.bedwars.Menus;

import org.bukkit.entity.Player;

public interface Menu {
    public void createMenu();
    public void openMenu(Player player);
    public String getMenuID();
}
