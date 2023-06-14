package mrkeith782.bedwars;

import lombok.Getter;
import mrkeith782.bedwars.commands.BedwarsCommands;
import mrkeith782.bedwars.game.BedwarsGame;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {
    @Getter
    private static Bedwars instance;
    @Getter
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

    public void createNewGame() {
        bedwarsGame = new BedwarsGame();
        bedwarsGame.build();
    }

    public void closeGame() {
        bedwarsGame.closeGame();
        bedwarsGame = null;
    }
}
