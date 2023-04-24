package mrkeith782.bedwars.Commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.Managers.MenuManager;
import mrkeith782.bedwars.Menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MenuOpenCommand implements CommandExecutor {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final MenuManager mm = bedwars.getMm();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { //all of my commands atm are for players so i'm happy with this
            return false;
        }
        Player player = (Player) sender;

        List<Menu> menus = mm.getMenus();
        for (Menu menu : menus) {
            if (menu.getMenuID().equalsIgnoreCase(args[0])) {
                menu.openMenu(player);
                return true;
            }
        }
        return false;
    }
}
