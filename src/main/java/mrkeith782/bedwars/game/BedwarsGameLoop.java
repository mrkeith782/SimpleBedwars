package mrkeith782.bedwars.game;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class BedwarsGameLoop {
    BukkitRunnable gameLoop;
    BedwarsGame game;

    /**
     * Creates the Bedwars Game Loop. The game loop is what keeps track of generator progress,
     * scoreboards, game progress, and if the win condition has been met for the game yet.
     */
    public BedwarsGameLoop() {

        this.gameLoop = new BukkitRunnable() {
            int CURRENT_TIME = 0;

            @Override
            public void run() {
                if (game == null) {
                    game = Bedwars.getInstance().getBedwarsGame();
                }

                if (CURRENT_TIME == 0) {
                    removeScoreboardTime("Diamond II in ", 0);
                }
                else if (CURRENT_TIME < 300) {
                    removeScoreboardTime("Diamond II in ", 300 - CURRENT_TIME);
                    addNewScoreboardTime("Diamond II in ", 300 - CURRENT_TIME - 1);
                }
                else if (CURRENT_TIME == 300) {
                    removeScoreboardTime("Diamond II in ", 0);
                    addNewScoreboardTime("Emerald II in ", 600 - CURRENT_TIME - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Diamond generators upgraded to level 2!"));
                    game.gameStatus = GameStatus.PHASE_1;
                }
                else if (CURRENT_TIME < 600) {
                    removeScoreboardTime("Emerald II in ", 600 - CURRENT_TIME);
                    addNewScoreboardTime("Emerald II in ", 600 - CURRENT_TIME - 1);
                }
                else if (CURRENT_TIME == 600) {
                    removeScoreboardTime("Emerald II in ", 0);
                    addNewScoreboardTime("Diamond III in ", 900 - CURRENT_TIME - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Emerald generators upgraded to level 2!"));
                    game.gameStatus = GameStatus.PHASE_2;
                }
                else if (CURRENT_TIME < 900) {
                    removeScoreboardTime("Diamond III in ", 900 - CURRENT_TIME);
                    addNewScoreboardTime("Diamond III in ", 900 - CURRENT_TIME - 1);
                }
                else if (CURRENT_TIME == 900) {
                    removeScoreboardTime("Diamond III in ", 0);
                    addNewScoreboardTime("Emerald III in ", 1200 - CURRENT_TIME - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Diamond generators upgraded to level 3!"));
                    game.gameStatus = GameStatus.PHASE_3;
                }
                else if (CURRENT_TIME < 1200) {
                    removeScoreboardTime("Emerald III in ", 1200 - CURRENT_TIME);
                    addNewScoreboardTime("Emerald III in ", 1200 - CURRENT_TIME - 1);
                }
                else if (CURRENT_TIME == 1200) {
                    removeScoreboardTime("Emerald III in ", 0);
                    addNewScoreboardTime("Beds break in ", 1500 - CURRENT_TIME - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Emerald generators upgraded to level 3!"));
                    game.gameStatus = GameStatus.PHASE_4;
                }
                else if (CURRENT_TIME < 1500) {
                    removeScoreboardTime("Beds break in ", 1500 - CURRENT_TIME);
                    addNewScoreboardTime("Beds break in ", 1500 - CURRENT_TIME - 1);
                }
                else if (CURRENT_TIME == 1500) {
                    removeScoreboardTime("Beds break in ", 0);
                    addNewScoreboardTime("Game ends in ", 1800 - CURRENT_TIME - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Beds broken!"));
                    game.gameStatus = GameStatus.PHASE_5;
                }
                else if (CURRENT_TIME < 1800) {
                    removeScoreboardTime("Game ends in ", 1800 - CURRENT_TIME);
                    addNewScoreboardTime("Game ends in ", 1800 - CURRENT_TIME - 1);
                }
                else if (CURRENT_TIME == 1800) {
                    game.gameStatus = GameStatus.ENDING;
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Game ended fuckos!"));
                }

                // Let's see if we can drop items, and do so here
                game.generatorManager.checkAndDropItems(CURRENT_TIME);

                List<BedwarsPlayer> bedwarsPlayers = game.getBedwarsPlayers();
                if (bedwarsPlayers == null || bedwarsPlayers.size() == 0) {
                    return;
                }

                for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
                    if (bedwarsPlayer.needsUpdate) {
                        updateScoreboardValues(bedwarsPlayer);
                    }
                }

                CURRENT_TIME++;
            }
        };
    }

    /**
     * Removes the current event and time entry from the scoreboard
     * @param prepend Prepended value for the scoreboard
     * @param value Current time that is being displayed
     */
    private void removeScoreboardTime(String prepend, int value) {
        List<BedwarsPlayer> bedwarsPlayers = game.getBedwarsPlayers();
        if (bedwarsPlayers == null || bedwarsPlayers.size() == 0) {
            return;
        }
        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            Scoreboard scoreboard = Bedwars.getInstance().getBedwarsGame().getScoreboardManager().getScoreboard(bedwarsPlayer.getPlayer());
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

    /**
     * Adds the current event, and its associated time to the scoreboard.
     * @param prepend Current event, prepended to the time
     * @param value Time value until the event occurs
     */
    private void addNewScoreboardTime(String prepend, int value) {
        List<BedwarsPlayer> bedwarsPlayers = game.getBedwarsPlayers();
        if (bedwarsPlayers == null || bedwarsPlayers.size() == 0) {
            return;
        }

        for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
            Scoreboard scoreboard = Bedwars.getInstance().getBedwarsGame().getScoreboardManager().getScoreboard(bedwarsPlayer.getPlayer());
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

    /**
     * Update the player's scoreboard values if they need updated.
     * @param bedwarsPlayer BedwarsPlayer to update scoreboard values for.
     */
    private void updateScoreboardValues(BedwarsPlayer bedwarsPlayer) {
        Scoreboard scoreboard = Bedwars.getInstance().getBedwarsGame().getScoreboardManager().getScoreboard(bedwarsPlayer.getPlayer());
        if (scoreboard == null) {
            return;
        }
        Objective objective = scoreboard.getObjective("Identifier");
        if (objective == null) {
            return;
        }

        // We're making the naive assumption that the player's stats have only increased by one
        scoreboard.resetScores(TextUtil.parseColoredString("Kills: %%green%%" + (bedwarsPlayer.getKills() - 1)));
        Score score = objective.getScore(TextUtil.parseColoredString("Kills: %%green%%" + (bedwarsPlayer.getKills())));
        score.setScore(5);

        scoreboard.resetScores(TextUtil.parseColoredString("Final Kills: %%green%%" + (bedwarsPlayer.getFinalKills() - 1)));
        score = objective.getScore(TextUtil.parseColoredString("Final Kills: %%green%%" + (bedwarsPlayer.getFinalKills())));
        score.setScore(4);

        scoreboard.resetScores(TextUtil.parseColoredString("Beds Broken: %%green%%" + (bedwarsPlayer.getBedsBroken() - 1)));
        score = objective.getScore(TextUtil.parseColoredString("Beds Broken: %%green%%" + (bedwarsPlayer.getBedsBroken())));
        score.setScore(3);

        bedwarsPlayer.setNeedsUpdate(false);
    }

    /**
     * Starts the game loop. This should be used after players have been added to the game.
     */
    public void startGameLoop() {
        this.gameLoop.runTaskTimer(Bedwars.getInstance(), 0, 20L);
    }

    /**
     * Stop the currently running game loop. This should be used when the game is over.
     */
    public void stopGameLoop() {
        this.gameLoop.cancel();
    }
}
