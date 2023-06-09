package mrkeith782.bedwars.game;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.listeners.InventoryClickListener;
import mrkeith782.bedwars.listeners.NPCLeftClickListener;
import mrkeith782.bedwars.listeners.TeamChestListener;
import mrkeith782.bedwars.managers.ArmorStandManager;
import mrkeith782.bedwars.managers.BedwarsScoreboardManager;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.managers.NPCManager;
import mrkeith782.bedwars.menus.ShopMenu;
import mrkeith782.bedwars.menus.UpgradeMenu;
import mrkeith782.bedwars.npcs.ShopNPC;
import mrkeith782.bedwars.npcs.UpgradeNPC;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

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
        //Initialize our managers for the game.
        this.armorStandManager = new ArmorStandManager();
        this.scoreboardManager = new BedwarsScoreboardManager();
        this.npcManager = new NPCManager();
        this.menuManager = new MenuManager();
    }

    public void build() {
        this.gameStatus = GameStatus.BUILDING;

        //Initialize our menus for the game
        this.menuManager.registerMenu(new ShopMenu());
        this.menuManager.registerMenu(new UpgradeMenu());

        //Copy and create our world for the game.
        initializeWorld(new File("C:\\1.19.4 server\\plugins\\bedwars\\bedwars_world"), new File(Bukkit.getWorldContainer(), "bedwars_world"));

        new WorldCreator("bedwars_world").createWorld();
        if (Bukkit.getWorld("bedwars_world") == null) {
            this.gameStatus = GameStatus.FAILED;
            return;
        }

        //TODO: FastAsyncWorldEdit API usage here so we don't hang the main thread lmao
        this.preGameSpawn = new Location(Bukkit.getWorld("bedwars_world"), 0, 120, 0);

        //Init our teams
        initializeTeams();

        //Init our shop menu detections
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new NPCLeftClickListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new TeamChestListener(), Bedwars.getInstance());
        this.gameStatus = GameStatus.PREGAME;
    }

    /**
     * Literally stole this code from https://www.spigotmc.org/threads/world-copy.37932/
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
                    if (files != null) {
                        for (String file : files) { //recursively goes through the directory
                            File srcFile = new File(source, file);
                            File destFile = new File(target, file);
                            initializeWorld(srcFile, destFile);
                        }
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

    /**
     * Currently, creates only two teams as a PoC.
     */
    private void initializeTeams() {
        //This is a bunch of hard coded values which isn't exactly nice. It'd be better if we read this in from a config
        World world = Bukkit.getWorld("bedwars_world");
        BedwarsTeam RED_TEAM = new BedwarsTeam(
                "Red",
                Color.RED,
                new Location(world, -33, 66, -64),
                new Location(world, -31, 66, -71),
                new Location(world, -36, 66, -70),
                new Location(world, -33.5, 66, -76),
                new Location(world, -28.5, 66, -73),
                new Location(world, -38.5, 66, -73)
        );
        BedwarsTeam BLUE_TEAM = new BedwarsTeam(
                "Blue",
                Color.BLUE,
                new Location(world, 33, 66, -65),
                new Location(world, 35, 66, -71),
                new Location(world, 29, 66, -70),
                new Location(world, 32.5, 66, -76),
                new Location(world, 37, 66, -73),
                new Location(world, 27, 66, -73)
        );
        //Cool! I could do the rest of the teams but fuck that :)
        bedwarsTeams.add(RED_TEAM);
        bedwarsTeams.add(BLUE_TEAM);
    }

    /**
     * Unloads the bedwars world and deletes it.
     */
    private void deleteBedwarsWorld() {
        World world = Bukkit.getWorld("bedwars_world");
        World defaultWorld = Bukkit.getWorld("world"); //TODO: what if the default world isn't world?

        if (world == null) {
            return;
        }

        if (defaultWorld != null) {
            for (Player player : world.getPlayers()) {
                player.teleport(defaultWorld.getSpawnLocation());
            }
        }

        Bukkit.getServer().unloadWorld(world, false);
        deleteDirectory(new File(Bukkit.getWorldContainer(), "bedwars_world"));
    }

    /**
     * Used to delete the physical files of the BedwarsWorld. Stolen from https://www.baeldung.com/java-delete-directory
     * @param directoryToBeDeleted Take a fucking guess
     * @return True if could be deleted
     */
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    /**
     * Starts the game and the game loop.
     */
    public void startGame() {
        //Assign players to teams
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            BedwarsTeam team = this.getSmallestTeam();
            //Double check to make sure we actually init'd our teams
            if (team == null) {
                this.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%red%%Failed to initialize teams. Game start aborted ):"));
                this.gameStatus = GameStatus.FAILED;
                this.closeGame();
                return;
            }

            //Assign team to player
            BedwarsTeam bedwarsTeam = getSmallestTeam();
            bedwarsTeam.addPlayerToTeam(bedwarsPlayer);
            bedwarsPlayer.setTeam(bedwarsTeam);

            //Update scoreboards
        }

        World world = Bukkit.getWorld("bedwars_world");
        if (world == null) {
            this.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%red%%Failed to get world. Game start aborted ):"));
            this.gameStatus = GameStatus.FAILED;
            this.closeGame();
            return;
        }

        for (BedwarsTeam bedwarsTeam : bedwarsTeams) {
            //Teleport all players to their team's generator
            List<BedwarsPlayer> players = bedwarsTeam.getAllTeamPlayers();
            for (BedwarsPlayer bedwarsPlayer : players) {
                Player player = Bukkit.getPlayer(bedwarsPlayer.getPlayerUUID());
                if (player == null) {
                    continue;
                }

                player.teleport(bedwarsTeam.getTeamGeneratorLocation());
            }

            //Spawn shop NPC with associated displays
            Location shopLoc = bedwarsTeam.getShopLocation();
            npcManager.spawnAndStoreNPC(
                    new ShopNPC(90),
                    bedwarsTeam.getShopLocation()
            );

            armorStandManager.spawnNewTextDisplay(
                    shopLoc.clone().add(0, 2.6, 0),
                    "%%aqua%%SHOP",
                    bedwarsTeam.teamDisplayName + "_SHOP_NPC_1",
                    90
            );

            armorStandManager.spawnNewTextDisplay(
                    shopLoc.clone().add(0, 2.4, 0),
                    "%%yellow%%%%bold%%RIGHT CLICK",
                    bedwarsTeam.teamDisplayName + "_SHOP_NPC_2",
                    90
            );

            //Spawn upgrade NPC with associated displays
            Location upgradeLoc = bedwarsTeam.getUpgradesLocation();
            npcManager.spawnAndStoreNPC(
                    new UpgradeNPC(270),
                    upgradeLoc
            );

            armorStandManager.spawnNewTextDisplay(
                    upgradeLoc.clone().add(0, 2.7, 0),
                    "%%aqua%%SOLO",
                    bedwarsTeam.teamDisplayName + "_UPGRADE_NPC_1",
                    270
            );

            armorStandManager.spawnNewTextDisplay(
                    upgradeLoc.clone().add(0, 2.5, 0),
                    "%%aqua%%UPGRADES",
                    bedwarsTeam.teamDisplayName + "_UPGRADE_NPC_2",
                    270
            );

            armorStandManager.spawnNewTextDisplay(
                    upgradeLoc.clone().add(0, 2.3, 0),
                    "%%yellow%%%%bold%%RIGHT CLICK",
                    bedwarsTeam.teamDisplayName + "_UPGRADE_NPC_2",
                    270
            );

            //Place chests and echests
            bedwarsTeam.getChestLocation().getBlock().setType(Material.CHEST);
            bedwarsTeam.getEnderChestLocation().getBlock().setType(Material.ENDER_CHEST);
        }

        this.gameStatus = GameStatus.STARTED;
    }

    /**
     * Gets the first currently registered team with the least amount of players.
     */
    @Nullable
    private BedwarsTeam getSmallestTeam() {
        return bedwarsTeams.stream()
            .min(Comparator.comparingInt(team -> team.getAllTeamPlayers().size()))
            .orElse(null);
    }

    /**
     * Elegantly remove the player from the game, and close the game
     */
    public void closeGame() {
        armorStandManager.removeAllArmorStands();
        armorStandManager.removeAllTextDisplays();
        this.armorStandManager = null;

        scoreboardManager.removeAllScoreboards();
        menuManager.removeAllMenus();

        npcManager.removeAllNPCs();
        this.npcManager = null;

        HandlerList.unregisterAll(Bedwars.getInstance());
        deleteBedwarsWorld();
    }

    /**
     * Adds a player to the current Bedwars game if they're allowed to join.
     * @param player Player to add to the game
     * @return True if the player cannot be added, false if not
     */
    public boolean addBedwarsPlayer(Player player) {
        if (this.gameStatus != GameStatus.PREGAME) {
            return false;
        }

        BedwarsPlayer bwPlayer = new BedwarsPlayer(player);
        bedwarsPlayers.add(bwPlayer);

        player.teleport(this.preGameSpawn);
        messageAllBedwarsPlayers(TextUtil.parseColoredString("%%aqua%%" + player.getName() + " %%yellow%%has joined the game!"));

        //Creates a scoreboard for the pre-game lobby
        List<String> spectatingScoreboard = new ArrayList<>();
        spectatingScoreboard.add(TextUtil.parseColoredString("%%yellow%%Pregame"));
        spectatingScoreboard.add(" ");
        spectatingScoreboard.add("%%yellow%%Waiting for players...");
        spectatingScoreboard.add(" ");
        spectatingScoreboard.add(" ");
        this.scoreboardManager.createNewScoreboard(player, spectatingScoreboard);
        this.scoreboardManager.updateScoreboardTitle(player, "%%yellow%%%%bold%%BEDWARS");
        this.scoreboardManager.refreshScoreboard(player);
        return true;
    }

    /**
     * Messages all players that are currently in the game.
     * @param string Message to send
     */
    public void messageAllBedwarsPlayers(String string) {
        bedwarsPlayers.forEach(bwPlayer -> Objects.requireNonNull(Bukkit.getPlayer(bwPlayer.getPlayerUUID())).sendMessage(string));
    }

    public BedwarsGame getBedwarsGame() {
        return this.bedwarsGame;
    }

    public ArmorStandManager getArmorStandManager() {
        return this.armorStandManager;
    }

    public BedwarsScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public MenuManager getMenuManager() {
        return this.menuManager;
    }

    public NPCManager getNpcManager() {
        return this.npcManager;
    }

    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public boolean contains(Player player) {
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            if (bedwarsPlayer.getPlayerUUID() == player.getUniqueId()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public BedwarsTeam getTeamByPlayer(Player player) {
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            if (bedwarsPlayer.getPlayerUUID() == player.getUniqueId()) {
                return bedwarsPlayer.getTeam();
            }
        }
        return null;
    }
}
