package mrkeith782.bedwars.game;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.player.BedwarsPlayer;
import mrkeith782.bedwars.game.team.BedwarsTeam;
import mrkeith782.bedwars.game.player.PlayerStatus;
import mrkeith782.bedwars.game.team.TeamStatus;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class BedwarsGameLoop {
    BukkitRunnable gameLoop;
    BedwarsGame game;

    /**
     * Creates the Bedwars Game Loop. The game loop is what keeps track of generator progress,
     * scoreboards, game progress, and if the win condition has been met.
     */
    public BedwarsGameLoop() {
        this.gameLoop = new BukkitRunnable() {
            int currentGameTime = 0;

            @Override
            public void run() {
                // Called here because the game isn't init'd yet until after this is constructed
                if (game == null) {
                    game = Bedwars.getInstance().getBedwarsGame();
                }

                if (currentGameTime < 300) {
                    removeScoreboardTime("Diamond II in ", 300 - currentGameTime);
                    addNewScoreboardTime("Diamond II in ", 300 - currentGameTime - 1);
                }
                else if (currentGameTime == 300) {
                    removeScoreboardTime("Diamond II in ", 0);
                    addNewScoreboardTime("Emerald II in ", 600 - currentGameTime - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Diamond generators upgraded to level 2!"));
                    game.gameStatus = GameStatus.PHASE_1;
                }
                else if (currentGameTime < 600) {
                    removeScoreboardTime("Emerald II in ", 600 - currentGameTime);
                    addNewScoreboardTime("Emerald II in ", 600 - currentGameTime - 1);
                }
                else if (currentGameTime == 600) {
                    removeScoreboardTime("Emerald II in ", 0);
                    addNewScoreboardTime("Diamond III in ", 900 - currentGameTime - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Emerald generators upgraded to level 2!"));
                    game.gameStatus = GameStatus.PHASE_2;
                }
                else if (currentGameTime < 900) {
                    removeScoreboardTime("Diamond III in ", 900 - currentGameTime);
                    addNewScoreboardTime("Diamond III in ", 900 - currentGameTime - 1);
                }
                else if (currentGameTime == 900) {
                    removeScoreboardTime("Diamond III in ", 0);
                    addNewScoreboardTime("Emerald III in ", 1200 - currentGameTime - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Diamond generators upgraded to level 3!"));
                    game.gameStatus = GameStatus.PHASE_3;
                }
                else if (currentGameTime < 1200) {
                    removeScoreboardTime("Emerald III in ", 1200 - currentGameTime);
                    addNewScoreboardTime("Emerald III in ", 1200 - currentGameTime - 1);
                }
                else if (currentGameTime == 1200) {
                    removeScoreboardTime("Emerald III in ", 0);
                    addNewScoreboardTime("Beds break in ", 1500 - currentGameTime - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Emerald generators upgraded to level 3!"));
                    game.gameStatus = GameStatus.PHASE_4;
                }
                else if (currentGameTime < 1500) {
                    removeScoreboardTime("Beds break in ", 1500 - currentGameTime);
                    addNewScoreboardTime("Beds break in ", 1500 - currentGameTime - 1);
                }
                else if (currentGameTime == 1500) {
                    removeScoreboardTime("Beds break in ", 0);
                    addNewScoreboardTime("Game ends in ", 1800 - currentGameTime - 1);
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Beds broken!"));
                    game.gameStatus = GameStatus.PHASE_5;
                }
                else if (currentGameTime < 1800) {
                    removeScoreboardTime("Game ends in ", 1800 - currentGameTime);
                    addNewScoreboardTime("Game ends in ", 1800 - currentGameTime - 1);
                }
                else if (currentGameTime == 1800) {
                    game.gameStatus = GameStatus.ENDING;
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%Game ended fuckos!"));
                }

                // Let's see if we can drop items, and do so here
                game.generatorManager.checkAndDropItems(currentGameTime);

                List<BedwarsPlayer> bedwarsPlayers = game.getBedwarsPlayers();
                if (bedwarsPlayers == null || bedwarsPlayers.isEmpty()) {
                    return;
                }

                for (BedwarsPlayer bedwarsPlayer : bedwarsPlayers) {
                    if (bedwarsPlayer.getNeedsUpdate()) {
                        updateScoreboardValues(bedwarsPlayer);
                    }
                }

                for (BedwarsTeam bedwarsTeam : game.getBedwarsTeams()) {
                    if (bedwarsTeam.getNeedsUpdate()) {
                        updateTeamScoreboardValues(bedwarsTeam);
                    }
                }

                checkWinCondition();
                currentGameTime++;
            }
        };
    }

    private void updateTeamScoreboardValues(BedwarsTeam bedwarsTeam) {
        List<BedwarsPlayer> bedwarsPlayers = game.getBedwarsPlayers();
        if (bedwarsPlayers == null || bedwarsPlayers.isEmpty()) {
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

            // Let's see if updating this value causes the team to be out of players
            if (checkValidPlayers(bedwarsTeam) == 0) {
                bedwarsTeam.setTeamStatus(TeamStatus.DEAD);
            }

            // Change the team's state in the scoreboard, hopefully successfully.
            // It's really weird that I hard coded this, would be better to absolutely fucking not do this
            if (bedwarsTeam.getTeamDisplayName().equalsIgnoreCase("Red")) {
                String redCheck = TextUtil.parseColoredString("%%red%%R %%white%%Red: %%green%%✓");
                if (bedwarsTeam.getTeamStatus() == TeamStatus.BED_BROKEN) {
                    int playersLeft = bedwarsTeam.getTeamPlayers().size();
                    scoreboard.resetScores(redCheck);
                    scoreboard.resetScores(TextUtil.parseColoredString(TextUtil.parseColoredString("%%red%%R %%white%%Red: %%green%%" + (playersLeft + 1))));
                    Score score = objective.getScore(TextUtil.parseColoredString("%%red%%R %%white%%Red: %%green%%" + playersLeft));
                    score.setScore(8);
                }
                if (bedwarsTeam.getTeamStatus() == TeamStatus.DEAD) {
                    scoreboard.resetScores(redCheck);
                    scoreboard.resetScores(TextUtil.parseColoredString(TextUtil.parseColoredString("%%red%%R %%white%%Red: %%green%%1")));
                    Score score = objective.getScore(TextUtil.parseColoredString("%%red%%R %%white%%Red: %%red%%✗"));
                    score.setScore(8);
                }
            } else if (bedwarsTeam.getTeamDisplayName().equalsIgnoreCase("Blue")) {
                String blueCheck = TextUtil.parseColoredString("%%blue%%B %%white%%Blue: %%green%%✓");
                scoreboard.resetScores(blueCheck);
                if (bedwarsTeam.getTeamStatus() == TeamStatus.BED_BROKEN) {
                    int playersLeft = bedwarsTeam.getTeamPlayers().size();
                    scoreboard.resetScores(blueCheck);
                    scoreboard.resetScores(TextUtil.parseColoredString(TextUtil.parseColoredString("%%blue%%B %%white%%Blue: %%green%%" + (playersLeft + 1))));
                    Score score = objective.getScore(TextUtil.parseColoredString("%%blue%%B %%white%%Blue: %%green%%" + playersLeft));
                    score.setScore(7);
                }
                if (bedwarsTeam.getTeamStatus() == TeamStatus.DEAD) {
                    scoreboard.resetScores(blueCheck);
                    scoreboard.resetScores(TextUtil.parseColoredString(TextUtil.parseColoredString("%%blue%%B %%white%%Blue: %%green%%1")));
                    Score score = objective.getScore(TextUtil.parseColoredString("%%blue%%B %%white%%Blue: %%red%%✗"));
                    score.setScore(7);
                }
            }
        }

        // We've updated everyone's scoreboard, let's update flag so we don't have to go through this logic again
        bedwarsTeam.setNeedsUpdate(false);
    }

    /**
     * Removes the current event and time entry from the scoreboard
     * @param prepend Prepended value for the scoreboard
     * @param value Current time that is being displayed
     */
    private void removeScoreboardTime(String prepend, int value) {
        List<BedwarsPlayer> bedwarsPlayers = game.getBedwarsPlayers();
        if (bedwarsPlayers == null || bedwarsPlayers.isEmpty()) {
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
        if (bedwarsPlayers == null || bedwarsPlayers.isEmpty()) {
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
     * Check how many players are not final killed on a team
     * @param bedwarsTeam BedwarsTeam to check players
     * @return Amount of players left alive on the team
     */
    private int checkValidPlayers(BedwarsTeam bedwarsTeam) {
        return (int) bedwarsTeam.getTeamPlayers().stream()
                .filter(player -> player.getStatus() != PlayerStatus.FINAL_DEAD)
                .count();
    }

    /**
     * Loop through all our teams to see if only one remains.
     */
    private void checkWinCondition() {
        int teamsAlive = 0;
        BedwarsTeam currTeam = null;

        // Loop through all our teams, collecting teams that are alive.
        for (BedwarsTeam bedwarsTeam : game.getBedwarsTeams()) {
            if (bedwarsTeam.getTeamStatus() != TeamStatus.DEAD) {
                teamsAlive++;
                currTeam = bedwarsTeam;
            }
        }

        // If only one team is alive, store the team and return it.
        if (teamsAlive == 1) {
            game.gameStatus = GameStatus.ENDING;
            game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%red%%Game is over! %%yellow%%" + currTeam.getTeamDisplayName() + " team won!"));

            Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> Bedwars.getInstance().closeGame(), 100L);
            stopGameLoop();
        }
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
