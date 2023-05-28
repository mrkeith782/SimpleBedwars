package mrkeith782.bedwars;

import mrkeith782.bedwars.commands.ArmorStandCommand;
import mrkeith782.bedwars.commands.FuckWithPackets;
import mrkeith782.bedwars.commands.MenuOpenCommand;
import mrkeith782.bedwars.commands.ScoreboardOpenCommand;
import mrkeith782.bedwars.listeners.InventoryClickListener;
import mrkeith782.bedwars.managers.ArmorStandManager;
import mrkeith782.bedwars.managers.BedwarsScoreboardManager;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.menus.ShopMenu;
import mrkeith782.bedwars.menus.UpgradeMenu;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {
    private static Bedwars instance;
    public ArmorStandManager asm;
    public MenuManager mm;
    public BedwarsScoreboardManager sbm;

    @Override
    public void onEnable() {
        instance = this;
        asm = new ArmorStandManager();
        mm = new MenuManager();
        sbm = new BedwarsScoreboardManager();

        getCommand("placearmorstand").setExecutor(new ArmorStandCommand());
        getCommand("editarmorstand").setExecutor(new ArmorStandCommand());
        getCommand("spawntextdisplay").setExecutor(new ArmorStandCommand());
        getCommand("openmenu").setExecutor(new MenuOpenCommand());
        getCommand("openscoreboard").setExecutor(new ScoreboardOpenCommand());
        getCommand("fuckwithpackets").setExecutor(new FuckWithPackets());

        mm.registerMenu(new ShopMenu());
        mm.registerMenu(new UpgradeMenu());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
    }

    @Override
    public void onDisable() {
        asm.removeAllArmorStands();
        asm.removeAllTextDisplays();
    }

    public static Bedwars getInstance() {
        return instance;
    }

    public ArmorStandManager getAsm() {
        return asm;
    }
    public MenuManager getMm() {
        return mm;
    }
    public BedwarsScoreboardManager getSbm() {
        return sbm;
    }
}
