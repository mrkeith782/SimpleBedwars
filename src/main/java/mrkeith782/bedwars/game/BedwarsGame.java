package mrkeith782.bedwars.game;

import mrkeith782.bedwars.managers.ArmorStandManager;
import mrkeith782.bedwars.managers.BedwarsScoreboardManager;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.managers.NPCManager;
import mrkeith782.bedwars.menus.ShopMenu;
import mrkeith782.bedwars.menus.UpgradeMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BedwarsGame {
    BedwarsGame bedwarsGame = this;
    GameStatus gameStatus;

    public ArmorStandManager armorStandManager;
    public BedwarsScoreboardManager scoreboardManager;
    public MenuManager menuManager;
    public NPCManager npcManager;

    final List<BedwarsPlayer> bedwarsPlayers = new ArrayList<>();
    final List<BedwarsTeam> bedwarsTeams = new ArrayList<>();

    Location preGameSpawn;

    public BedwarsGame() {
        this.gameStatus = GameStatus.BUILDING;

        //Initialize our managers for the game.
        this.armorStandManager = new ArmorStandManager();
        this.scoreboardManager = new BedwarsScoreboardManager();
        this.npcManager = new NPCManager();

        this.menuManager = new MenuManager();
        menuManager.registerMenu(new ShopMenu());
        menuManager.registerMenu(new UpgradeMenu());

        //Copy and create our world for the game.
        initializeWorld(new File("C:\\1.19.4 server\\plugins\\bedwars\\bedwars_world"), new File(Bukkit.getWorldContainer(), "bedwars_world"));

        new WorldCreator("bedwars_world").createWorld();
        if (Bukkit.getWorld("bedwars_world") == null) {
            this.gameStatus = GameStatus.FAILED;
            return;
        }

        this.preGameSpawn = new Location(Bukkit.getWorld("bedwars_world"), 0, 100, 0);
        this.gameStatus = GameStatus.PREGAME;
    }

    /**
     * Literally stole this code from <a href="https://www.spigotmc.org/threads/world-copy.37932/">here</a>
     * @param source Location to copy from
     * @param target Location to copy to
     */
    private void initializeWorld(File source, File target) {
        try {
            List<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists()) {
                        if (!target.mkdirs()) {
                            throw new IOException("Couldn't create world directory!");
                        }
                    }

                    String[] files = source.list();
                    for (String file : files) { //recursively goes through the directory
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        initializeWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = Files.newInputStream(source.toPath());
                    OutputStream out = Files.newOutputStream(target.toPath());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeTeams() {
        return;
    }

    private void deleteBedwarsWorld() {
        Bukkit.unloadWorld("bedwars_world", false);
    }

    public void closeGame() {
        armorStandManager.removeAllArmorStands();
        armorStandManager.removeAllTextDisplays();
        this.armorStandManager = null;

        scoreboardManager.removeAllScoreboards();
        menuManager.removeAllMenus();

        npcManager.removeAllNPCs();
        this.npcManager = null;

        deleteBedwarsWorld();
    }

    public boolean addBedwarsPlayer(Player player) {
        if (this.gameStatus != GameStatus.PREGAME) {
            return false;
        }

        BedwarsPlayer bwPlayer = new BedwarsPlayer(player);
        bedwarsPlayers.add(bwPlayer);

        player.teleport(this.preGameSpawn);
        return true;
    }

    public BedwarsGame getBedwarsGame() {
        return bedwarsGame;
    }

    public ArmorStandManager getArmorStandManager() {
        return this.armorStandManager;
    }

    public BedwarsScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }
}
