package mrkeith782.bedwars;

import mrkeith782.bedwars.commands.*;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.listeners.InventoryClickListener;
import mrkeith782.bedwars.listeners.NPCLeftClickListener;
import mrkeith782.bedwars.managers.ArmorStandManager;
import mrkeith782.bedwars.managers.BedwarsScoreboardManager;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.managers.NPCManager;
import mrkeith782.bedwars.menus.ShopMenu;
import mrkeith782.bedwars.menus.UpgradeMenu;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {
    private static Bedwars instance;
    public MenuManager mm;
    public BedwarsScoreboardManager sbm;
    public ArmorStandManager asm;
    public NPCManager npcm;
    public BedwarsGame bedwarsGame = null;

    @Override
    public void onEnable() {
        instance = this;
        mm = new MenuManager();
        sbm = new BedwarsScoreboardManager();
        npcm = new NPCManager();

        getCommand("placearmorstand").setExecutor(new ArmorStandCommand());
        getCommand("editarmorstand").setExecutor(new ArmorStandCommand());
        getCommand("spawntextdisplay").setExecutor(new ArmorStandCommand());
        getCommand("openmenu").setExecutor(new MenuOpenCommand());
        getCommand("openscoreboard").setExecutor(new ScoreboardOpenCommand());
        getCommand("fuckwithpackets").setExecutor(new FuckWithPackets());
        getCommand("generateisland").setExecutor(new GenerateIslandCommand());
        getCommand("bedwars").setExecutor(new BedwarsCommands());

        mm.registerMenu(new ShopMenu());
        mm.registerMenu(new UpgradeMenu());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new NPCLeftClickListener(), this);
    }

    @Override
    public void onDisable() {
        if (this.bedwarsGame == null) {
            return;
        } else {
            this.closeGame();
        }
        asm.removeAllArmorStands();
        asm.removeAllTextDisplays();
        npcm.removeAllNPCs();
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
    public NPCManager getNpcm() {
        return npcm;
    }
    public void createNewGame() {
        this.bedwarsGame = new BedwarsGame();
    }
    public void closeGame() {
        bedwarsGame.closeGame();
        this.bedwarsGame = null;
    }
    public BedwarsGame getBedwarsGame() {
        return this.bedwarsGame;
    }
}
