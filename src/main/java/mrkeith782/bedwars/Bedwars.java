package mrkeith782.bedwars;

import mrkeith782.bedwars.commands.BedwarsCommands;
import mrkeith782.bedwars.game.BedwarsGame;
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
