package mrkeith782.bedwars;

import mrkeith782.bedwars.commands.BedwarsCommands;
import mrkeith782.bedwars.game.BedwarsGame;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {
    private static Bedwars instance;
    public BedwarsGame bedwarsGame;

    @Override
    public void onEnable() {
        instance = this;

        getCommand("bedwars").setExecutor(new BedwarsCommands());
    }

    @Override
    public void onDisable() {
        if (bedwarsGame != null) {
            closeGame();
        }
    }

    public static Bedwars getInstance() {
        return instance;
    }

    public void createNewGame() {
        bedwarsGame = new BedwarsGame();
        bedwarsGame.build();
    }

    public void closeGame() {
        bedwarsGame.closeGame();
        bedwarsGame = null;
    }

    public BedwarsGame getBedwarsGame() {
        return bedwarsGame;
    }
}
