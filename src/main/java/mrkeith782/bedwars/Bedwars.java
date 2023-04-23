package mrkeith782.bedwars;

import mrkeith782.bedwars.Commands.ArmorStandCommand;
import mrkeith782.bedwars.Commands.MenuOpenCommand;
import mrkeith782.bedwars.Managers.ArmorStandManager;
import mrkeith782.bedwars.Managers.MenuManager;
import mrkeith782.bedwars.Menus.ShopMenu;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {
    private static Bedwars instance;
    public ArmorStandManager asm;
    public MenuManager mm;

    @Override
    public void onEnable() {
        instance = this;
        asm = new ArmorStandManager();
        mm = new MenuManager();

        getCommand("placearmorstand").setExecutor(new ArmorStandCommand());
        getCommand("printarmorstands").setExecutor(new ArmorStandCommand());
        getCommand("editarmorstand").setExecutor(new ArmorStandCommand());
        getCommand("openmenu").setExecutor(new MenuOpenCommand());

        mm.registerMenu(new ShopMenu());
    }

    @Override
    public void onDisable() {
        asm.removeAllArmorStands();
    }

    public static Bedwars getInstance() {
        return instance;
    }

    public ArmorStandManager getAsm() {
        return asm;
    }
    public MenuManager getMm() { return mm; }
}
