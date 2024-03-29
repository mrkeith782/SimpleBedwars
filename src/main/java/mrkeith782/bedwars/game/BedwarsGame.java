package mrkeith782.bedwars.game;

import lombok.Getter;
import lombok.Setter;
import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.player.BedwarsPlayer;
import mrkeith782.bedwars.game.team.BedwarsTeam;
import mrkeith782.bedwars.listeners.*;
import mrkeith782.bedwars.managers.*;
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
    @Getter
    @Setter
    public GameStatus gameStatus;
    @Getter
    final ArmorStandManager armorStandManager;
    @Getter
    final BedwarsScoreboardManager scoreboardManager;
    @Getter
    final MenuManager menuManager;
    @Getter
    final NPCManager npcManager;
    @Getter
    final GeneratorManager generatorManager;
    @Getter
    final List<BedwarsPlayer> bedwarsPlayers = new ArrayList<>();
    @Getter
    final List<BedwarsTeam> bedwarsTeams = new ArrayList<>();
    BedwarsGameLoop gameLoop;

    Location preGameSpawn;

    public BedwarsGame() {
        // Initialize our managers for the game.
        this.gameStatus = GameStatus.BUILDING;
        this.armorStandManager = new ArmorStandManager();
        this.scoreboardManager = new BedwarsScoreboardManager();
        this.npcManager = new NPCManager();
        this.menuManager = new MenuManager();
        this.generatorManager = new GeneratorManager();
        this.gameLoop = new BedwarsGameLoop();
    }

    /**
     * Called after creation, and before players are allowed in the game.
     */
    public void build() {
        // Initialize our menus for the game
        this.menuManager.registerMenu(new ShopMenu());
        this.menuManager.registerMenu(new UpgradeMenu());

        // Copy and create our world for the game.
        initializeWorld(new File(System.getProperty("user.dir") + "\\plugins\\bedwars\\bedwars_world"), new File(Bukkit.getWorldContainer(), "bedwars_world"));

        Bukkit.getServer().createWorld(new WorldCreator("bedwars_world").generatorSettings("2;0;12"));

        if (Bukkit.getWorld("bedwars_world") == null) {
            this.gameStatus = GameStatus.FAILED;
            return;
        }

        // TODO: FastAsyncWorldEdit API usage here so we don't hang the main thread lmao
        this.preGameSpawn = new Location(Bukkit.getWorld("bedwars_world"), 0, 120, 0);

        // Init our teams
        initializeTeams();

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new NPCLeftClickListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new TeamChestListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new TeamDamageListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new BedBreakListener(), Bedwars.getInstance());
        this.gameStatus = GameStatus.PREGAME;
    }

    /**
     * Literally stole this code from <a href="https://www.spigotmc.org/threads/world-copy.37932/">...</a>
     *
     * @param source Location to copy from
     * @param target Location to copy to
     */
    private void initializeWorld(File source, File target) {
        try {
            List<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists() && !target.mkdirs()) {
                        throw new IOException("Couldn't create world directory!");
                    }

                    String[] files = source.list();
                    if (files != null) {
                        for (String file : files) { // recursively goes through the directory
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
        // This is a bunch of hard coded values which isn't exactly nice. It'd be better if we read this in from a config
        World world = Bukkit.getWorld("bedwars_world");
        BedwarsTeam redTeam = new BedwarsTeam(
                "Red",
                Color.RED,
                new Location(world, -33, 66, -64),
                new Location(world, -31, 66, -71),
                new Location(world, -36, 66, -70),
                new Location(world, -33.5, 66, -76),
                new Location(world, -28.5, 66, -73),
                new Location(world, -38.5, 66, -73)
        );
        BedwarsTeam blueTeam = new BedwarsTeam(
                "Blue",
                Color.BLUE,
                new Location(world, 33, 66, -64),
                new Location(world, 35, 66, -71),
                new Location(world, 29, 66, -70),
                new Location(world, 32.5, 66, -76),
                new Location(world, 37, 66, -73),
                new Location(world, 27, 66, -73)
        );
        // Cool! I could do the rest of the teams but fuck that :)
        bedwarsTeams.add(redTeam);
        bedwarsTeams.add(blueTeam);
    }

    /**
     * Unloads the bedwars world and deletes it.
     */
    private void deleteBedwarsWorld() {
        World world = Bukkit.getWorld("bedwars_world");

        if (world == null) {
            return;
        }

        Bukkit.getServer().unloadWorld(world, false);
        deleteDirectory(new File(Bukkit.getWorldContainer(), "bedwars_world"));
    }

    /**
     * Used to delete the physical files of the BedwarsWorld. Stolen from <a href="https://www.baeldung.com/java-delete-directory">...</a>
     *
     * @param directoryToBeDeleted Take a fucking guess
     *
     * @return True if could be deleted
     */
    private boolean deleteDirectory(File directoryToBeDeleted) {
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
        // Remove the pregame scoreboard for all the players
        scoreboardManager.removeAllScoreboards();

        // Assign players to teams
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            BedwarsTeam team = getSmallestTeam();

            // Double check to make sure we actually init'd our teams
            if (team == null) {
                this.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%red%%Failed to initialize teams. Game start aborted ):"));
                this.gameStatus = GameStatus.FAILED;
                this.closeGame();
                return;
            }

            team.addPlayerToTeam(bedwarsPlayer);
            bedwarsPlayer.setTeam(team);

            // Update scoreboards
            Player player = bedwarsPlayer.getPlayer();

            List<String> spectatingScoreboard = new ArrayList<>();
            spectatingScoreboard.add(TextUtil.parseColoredString("%%gray%%" + BedwarsScoreboardManager.getPrettyDate()));
            spectatingScoreboard.add(" ");
            spectatingScoreboard.add("Diamond II in %%green%%5:00");
            spectatingScoreboard.add("  ");
            spectatingScoreboard.add(TextUtil.parseColoredString("%%red%%R %%white%%Red: %%green%%✓"));
            spectatingScoreboard.add(TextUtil.parseColoredString("%%blue%%B %%white%%Blue: %%green%%✓"));
            spectatingScoreboard.add("   ");
            spectatingScoreboard.add(TextUtil.parseColoredString("Kills: %%green%%0"));
            spectatingScoreboard.add(TextUtil.parseColoredString("Final Kills: %%green%%0"));
            spectatingScoreboard.add(TextUtil.parseColoredString("Beds Broken: %%green%%0"));
            spectatingScoreboard.add("    ");
            spectatingScoreboard.add("%%yellow%%mrkeith.yeet");

            this.scoreboardManager.createNewScoreboard(player, spectatingScoreboard);
            this.scoreboardManager.updateScoreboardTitle(player, "%%yellow%%%%bold%%BEDWARS");
            this.scoreboardManager.refreshScoreboard(player);
        }

        // Make sure that we actually have a world to send players to
        World world = Bukkit.getWorld("bedwars_world");
        if (world == null) {
            this.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%red%%Failed to get world. Game start aborted ):"));
            this.gameStatus = GameStatus.FAILED;
            Bedwars.getInstance().closeGame();
            return;
        }

        for (BedwarsTeam bedwarsTeam : bedwarsTeams) {
            // Teleport all players to their team's generator
            List<BedwarsPlayer> players = bedwarsTeam.getTeamPlayers();
            for (BedwarsPlayer bedwarsPlayer : players) {
                Player player = bedwarsPlayer.getPlayer();
                if (player == null) {
                    continue;
                }

                player.teleport(bedwarsTeam.getTeamGeneratorLocation());
                world.setSpawnLocation(0, 128, 0);
            }

            // Spawn shop NPC with associated displays
            Location shopLoc = bedwarsTeam.getShopLocation();
            npcManager.spawnAndStoreNPC(
                    new ShopNPC(90),
                    bedwarsTeam.getShopLocation()
            );

            armorStandManager.spawnNewTextDisplay(
                    shopLoc.clone().add(0, 2.6, 0),
                    "%%aqua%%SHOP",
                    bedwarsTeam.getTeamDisplayName() + "_SHOP_NPC_1",
                    90
            );

            armorStandManager.spawnNewTextDisplay(
                    shopLoc.clone().add(0, 2.4, 0),
                    "%%yellow%%%%bold%%RIGHT CLICK",
                    bedwarsTeam.getTeamDisplayName() + "_SHOP_NPC_2",
                    90
            );

            // Spawn upgrade NPC with associated displays
            Location upgradeLoc = bedwarsTeam.getUpgradesLocation();
            npcManager.spawnAndStoreNPC(
                    new UpgradeNPC(270),
                    upgradeLoc
            );

            armorStandManager.spawnNewTextDisplay(
                    upgradeLoc.clone().add(0, 2.7, 0),
                    "%%aqua%%SOLO",
                    bedwarsTeam.getTeamDisplayName() + "_UPGRADE_NPC_1",
                    270
            );

            armorStandManager.spawnNewTextDisplay(
                    upgradeLoc.clone().add(0, 2.5, 0),
                    "%%aqua%%UPGRADES",
                    bedwarsTeam.getTeamDisplayName() + "_UPGRADE_NPC_2",
                    270
            );

            armorStandManager.spawnNewTextDisplay(
                    upgradeLoc.clone().add(0, 2.3, 0),
                    "%%yellow%%%%bold%%RIGHT CLICK",
                    bedwarsTeam.getTeamDisplayName() + "_UPGRADE_NPC_2",
                    270
            );

            // Place chests and echests
            bedwarsTeam.getChestLocation().getBlock().setType(Material.CHEST);
            bedwarsTeam.getEnderChestLocation().getBlock().setType(Material.ENDER_CHEST);

            // Generator location for the team
            generatorManager.addNewGenerator(bedwarsTeam.getTeamDisplayName(), bedwarsTeam.getTeamGeneratorLocation());
        }

        // Diamond / emerald generator locations
        generatorManager.addNewGenerator("DIAMOND_1", new Location(world, 0.5, 65, -51.5));
        generatorManager.addNewGenerator("DIAMOND_2", new Location(world, 52.5, 65, 0.5));
        generatorManager.addNewGenerator("DIAMOND_3", new Location(world, 0.5, 65, 52.5));
        generatorManager.addNewGenerator("DIAMOND_4", new Location(world, -51.5, 65, 0.5));
        generatorManager.addNewGenerator("EMERALD_1", new Location(world, 12.5, 79, 12.5));
        generatorManager.addNewGenerator("EMERALD_2", new Location(world, 12.5, 79, -11.5));
        generatorManager.addNewGenerator("EMERALD_3", new Location(world, -11.5, 79, -11.5));
        generatorManager.addNewGenerator("EMERALD_4", new Location(world, -11.5, 79, 12.5));

        generatorManager.placeGenerators();
        generatorManager.startRotation();

        npcManager.buildNpcLookTask();

        gameLoop.startGameLoop();
        this.gameStatus = GameStatus.STARTED;
    }

    /**
     * Gets the first currently registered team with the least amount of players.
     */
    @Nullable
    private BedwarsTeam getSmallestTeam() {
        return bedwarsTeams.stream()
                .min(Comparator.comparingInt(team -> team.getTeamPlayers().size()))
                .orElse(null);
    }

    /**
     * Elegantly remove all the players from the game, then clean up all placed generators, holograms, and npcs.
     */
    public void closeGame() {
        gameLoop.stopGameLoop();

        // Remove the players that are currently in the world, and teleport them to the spawn
        World bedwarsWorld = Bukkit.getWorld("bedwars_world");
        World world = Bukkit.getWorld("world");
        if (bedwarsWorld != null && world != null) {
            for (Player player : bedwarsWorld.getPlayers()) {
                player.teleport(world.getSpawnLocation());
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
            }
        }

        armorStandManager.removeAllArmorStands();
        armorStandManager.removeAllTextDisplays();

        scoreboardManager.removeAllScoreboards();
        menuManager.removeAllMenus();

        npcManager.removeAllNPCs();
        npcManager.stopNpcLookTask();

        generatorManager.stopRotation();
        generatorManager.removeAllGenerators();

        HandlerList.unregisterAll(Bedwars.getInstance());
        deleteBedwarsWorld();
    }

    /**
     * Adds a player to the current Bedwars game if they're allowed to join.
     *
     * @param player Player to add to the game
     *
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

        // Creates a scoreboard for the pre-game lobby
        List<String> spectatingScoreboard = new ArrayList<>();
        spectatingScoreboard.add(TextUtil.parseColoredString("%%gray%%" + BedwarsScoreboardManager.getPrettyDate()));
        spectatingScoreboard.add(TextUtil.parseColoredString("%%yellow%%Pregame"));
        spectatingScoreboard.add(" ");
        spectatingScoreboard.add("%%yellow%%Waiting for players...");
        spectatingScoreboard.add("  ");
        spectatingScoreboard.add("   ");
        this.scoreboardManager.createNewScoreboard(player, spectatingScoreboard);
        this.scoreboardManager.updateScoreboardTitle(player, "%%yellow%%%%bold%%BEDWARS");
        this.scoreboardManager.refreshScoreboard(player);
        return true;
    }

    /**
     * Messages all players that are currently in the game.
     *
     * @param string Message to send
     */
    public void messageAllBedwarsPlayers(String string) {
        bedwarsPlayers.forEach(bedwarsPlayer -> Objects.requireNonNull(bedwarsPlayer.getPlayer()).sendMessage(string));
    }

    /**
     * Returns if the player is in the game
     * @param player Player to test
     * @return True if the player is in the game, false if not
     */
    public boolean contains(Player player) {
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            if (bedwarsPlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the bedwars team the player is currently on
     * @param player Player to get team for
     * @return Team the player is on, null if none / pre-game
     */
    @Nullable
    public BedwarsTeam getTeamByPlayer(Player player) {
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            if (bedwarsPlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
                return bedwarsPlayer.getTeam();
            }
        }
        return null;
    }

    @Nullable
    public BedwarsPlayer getBedwarsPlayer(Player player) {
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            if (bedwarsPlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
                return bedwarsPlayer;
            }
        }
        return null;
    }
}
