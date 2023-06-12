package mrkeith782.bedwars.game;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.listeners.InventoryClickListener;
import mrkeith782.bedwars.listeners.NPCLeftClickListener;
import mrkeith782.bedwars.listeners.PlayerDeathListener;
import mrkeith782.bedwars.listeners.TeamChestListener;
import mrkeith782.bedwars.managers.*;
import mrkeith782.bedwars.menus.ShopMenu;
import mrkeith782.bedwars.menus.UpgradeMenu;
import mrkeith782.bedwars.npcs.ShopNPC;
import mrkeith782.bedwars.npcs.UpgradeNPC;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

public class BedwarsGame {
    final BedwarsGame bedwarsGame = this;
    GameStatus gameStatus = GameStatus.BUILDING;

    final ArmorStandManager armorStandManager;
    final BedwarsScoreboardManager scoreboardManager;
    final MenuManager menuManager;
    final NPCManager npcManager;
    final GeneratorManager generatorManager;

    final List<BedwarsPlayer> bedwarsPlayers = new ArrayList<>();
    final List<BedwarsTeam> bedwarsTeams = new ArrayList<>();
    BukkitTask gameLoop = null;

    Location preGameSpawn;

    public BedwarsGame() {
        //Initialize our managers for the game.
        this.armorStandManager = new ArmorStandManager();
        this.scoreboardManager = new BedwarsScoreboardManager();
        this.npcManager = new NPCManager();
        this.menuManager = new MenuManager();
        this.generatorManager = new GeneratorManager();
    }

    /**
     * Called after creation, and before players are allowed in the game.
     */
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

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new NPCLeftClickListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new TeamChestListener(), Bedwars.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), Bedwars.getInstance());
        this.gameStatus = GameStatus.PREGAME;
    }

    /**
     * Defines the game loop that is stored in memory.
     * @return Game loop runnable
     */
    public BukkitRunnable createGameLoop() {
        return new BukkitRunnable() {
            int CURRENT_TIME = 0;

            @Override
            public void run() {
                // Jesus
                if (CURRENT_TIME == 0) {
                    removeScoreboardTime("Diamond II in ", 0);
                }
                if (CURRENT_TIME < 300) {
                    removeScoreboardTime("Diamond II in ", 300 - CURRENT_TIME);
                    addNewScoreboardTime("Diamond II in ", 300 - CURRENT_TIME - 1);
                } else if (CURRENT_TIME == 300) {
                    removeScoreboardTime("Diamond II in ", 0);
                    addNewScoreboardTime("Emerald II in ", 600 - CURRENT_TIME - 1);
                    messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Diamond generators upgraded to level 2!"));
                    gameStatus = GameStatus.PHASE_1;
                } else if (CURRENT_TIME < 600) {
                    removeScoreboardTime("Emerald II in ", 600 - CURRENT_TIME);
                    addNewScoreboardTime("Emerald II in ", 600 - CURRENT_TIME - 1);
                } else if (CURRENT_TIME == 600) {
                    removeScoreboardTime("Emerald II in ", 0);
                    addNewScoreboardTime("Diamond III in ", 900 - CURRENT_TIME - 1);
                    messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Emerald generators upgraded to level 2!"));
                    gameStatus = GameStatus.PHASE_2;
                } else if (CURRENT_TIME < 900) {
                    removeScoreboardTime("Diamond III in ", 900 - CURRENT_TIME);
                    addNewScoreboardTime("Diamond III in ", 900 - CURRENT_TIME - 1);
                } else if (CURRENT_TIME == 900) {
                    removeScoreboardTime("Diamond III in ", 0);
                    addNewScoreboardTime("Emerald III in ", 1200 - CURRENT_TIME - 1);
                    messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Diamond generators upgraded to level 3!"));
                    gameStatus = GameStatus.PHASE_3;
                } else if (CURRENT_TIME < 1200) {
                    removeScoreboardTime("Emerald III in ", 1200 - CURRENT_TIME);
                    addNewScoreboardTime("Emerald III in ", 1200 - CURRENT_TIME - 1);
                } else if (CURRENT_TIME == 1200) {
                    removeScoreboardTime("Emerald III in ", 0);
                    addNewScoreboardTime("Beds break in ", 1500 - CURRENT_TIME - 1);
                    messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Emerald generators upgraded to level 3!"));
                    gameStatus = GameStatus.PHASE_4;
                } else if (CURRENT_TIME < 1500) {
                    removeScoreboardTime("Beds break in ", 1500 - CURRENT_TIME);
                    addNewScoreboardTime("Beds break in ", 1500 - CURRENT_TIME - 1);
                } else if (CURRENT_TIME == 1500) {
                    removeScoreboardTime("Beds break in ", 0);
                    addNewScoreboardTime("Game ends in ", 1800 - CURRENT_TIME - 1);
                    messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Beds broken!"));
                    gameStatus = GameStatus.PHASE_5;
                } else if (CURRENT_TIME < 1800) {
                    removeScoreboardTime("Game ends in ", 1800 - CURRENT_TIME);
                    addNewScoreboardTime("Game ends in ", 1800 - CURRENT_TIME - 1);
                } else if (CURRENT_TIME == 1800) {
                    gameStatus = GameStatus.ENDING;
                    messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Game ended fuckos!"));
                }

                CURRENT_TIME++;

                //Let's see if we can drop items, and do so here
                generatorManager.checkAndDropItems(CURRENT_TIME);
            }

            private void removeScoreboardTime(String prepend, int value) {
                for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
                    Scoreboard scoreboard = scoreboardManager.getScoreboard(bedwarsPlayer.getPlayer());
                    if (scoreboard == null) {
                        continue;
                    }
                    Objective objective = scoreboard.getObjective("Identifier");
                    if (objective == null) {
                        continue;
                    }

                    scoreboard.resetScores(TextUtil.parseColoredString(prepend + "%%green%%" + TextUtil.formatPrettyTime(value)));
                }
            }

            private void addNewScoreboardTime(String prepend, int value) {
                for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
                    Scoreboard scoreboard = scoreboardManager.getScoreboard(bedwarsPlayer.getPlayer());
                    if (scoreboard == null) {
                        continue;
                    }
                    Objective objective = scoreboard.getObjective("Identifier");
                    if (objective == null) {
                        continue;
                    }

                    Score score = objective.getScore(TextUtil.parseColoredString(prepend + "%%green%%" + TextUtil.formatPrettyTime(value)));
                    score.setScore(10);
                }
            }
        };
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
            Player player = bedwarsPlayer.getPlayer();

            scoreboardManager.removeAllScoreboards();
            List<String> spectatingScoreboard = new ArrayList<>();
            spectatingScoreboard.add(TextUtil.parseColoredString("%%gray%%" + BedwarsScoreboardManager.getPrettyDate()));
            spectatingScoreboard.add(" ");
            spectatingScoreboard.add("Diamond II in %%green%%5:00");
            spectatingScoreboard.add("   ");
            spectatingScoreboard.add(TextUtil.parseColoredString("%%red%%R %%white%%Red: %%green%%✓"));
            spectatingScoreboard.add(TextUtil.parseColoredString("%%blue%%B %%white%%Blue: %%green%%✓"));
            spectatingScoreboard.add("    ");
            spectatingScoreboard.add(TextUtil.parseColoredString("Kills: %%green%%0"));
            spectatingScoreboard.add(TextUtil.parseColoredString("Final Kills: %%green%%0"));
            spectatingScoreboard.add(TextUtil.parseColoredString("Beds Broken: %%green%%0"));
            spectatingScoreboard.add("     ");
            spectatingScoreboard.add("%%yellow%%mrkeith.yeet");

            this.scoreboardManager.createNewScoreboard(player, spectatingScoreboard);
            this.scoreboardManager.updateScoreboardTitle(player, "%%yellow%%%%bold%%BEDWARS");
            this.scoreboardManager.refreshScoreboard(player);
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
                world.setSpawnLocation(0, 128, 0);
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

            //Generator location for the team
            generatorManager.addNewGenerator(bedwarsTeam.teamDisplayName, bedwarsTeam.getTeamGeneratorLocation());
        }

        //Diamond / emerald generator locations
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

        this.gameLoop = createGameLoop().runTaskTimer(Bedwars.getInstance(), 0L, 20L);
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
     * Elegantly remove all the players from the game, then clean up all placed generators, holograms, and npcs.
     */
    public void closeGame() {
        armorStandManager.removeAllArmorStands();
        armorStandManager.removeAllTextDisplays();

        scoreboardManager.removeAllScoreboards();
        menuManager.removeAllMenus();

        npcManager.removeAllNPCs();

        generatorManager.stopRotation();
        generatorManager.removeAllGenerators();

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
     * @param string Message to send
     */
    public void messageAllBedwarsPlayers(String string) {
        bedwarsPlayers.forEach(bwPlayer -> Objects.requireNonNull(Bukkit.getPlayer(bwPlayer.getPlayerUUID())).sendMessage(string));
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
}
