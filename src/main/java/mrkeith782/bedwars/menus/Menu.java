package mrkeith782.bedwars.menus;

import org.bukkit.entity.Player;

public interface Menu {
    String menuID = null;
    String menuName = null;

    public void createMenu();
    public void openMenu(Player player);
    public String getMenuID();
}
