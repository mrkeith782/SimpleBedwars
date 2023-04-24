package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.menus.Menu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
