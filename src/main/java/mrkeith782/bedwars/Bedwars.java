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
    public BedwarsGame bedwarsGame = null;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("bedwars").setExecutor(new BedwarsCommands());
    }

    @Override
    public void onDisable() {
        if (this.bedwarsGame != null) {
            this.closeGame();
        }
    }

    public static Bedwars getInstance() {
        return instance;
    }
    public void createNewGame() {
        this.bedwarsGame = new BedwarsGame();
        this.bedwarsGame.build();
    }
    public void closeGame() {
        bedwarsGame.closeGame();
        this.bedwarsGame = null;
    }
    public BedwarsGame getBedwarsGame() {
        return this.bedwarsGame;
    }
}
