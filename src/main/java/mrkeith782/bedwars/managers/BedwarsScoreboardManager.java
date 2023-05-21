package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BedwarsScoreboardManager {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final ScoreboardManager scoreboardManager;

    private final Map<String, Scoreboard> scoreboards = new HashMap<>();

    public BedwarsScoreboardManager() {
        scoreboardManager = bedwars.getServer().getScoreboardManager();
    }

    /**
     * Generates a new scoreboard that can be displayed to the player.
     * @param player Player to display to.
     * @param lines Lines, in order, that will be displayed to the player.
     */
    public void createNewScoreboard(Player player, List<String> lines) {
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("Identifier", Criteria.DUMMY, TextUtil.parseColoredString(" "));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int iteration = 0;
        for (String line : lines) {
            Score score = objective.getScore(TextUtil.parseColoredString(line));
            score.setScore(lines.size() - iteration);
            iteration++;
        }

        scoreboards.put(player.getName(), scoreboard);
    }

    /**
     * Refreshes the player's current scoreboard from memory.
     * @param player Player to refresh.
     */
    public void refreshScoreboard(Player player) {
        Scoreboard scoreboard = getScoreboard(player);
        if (scoreboard == null) {
            return;
        }
        player.setScoreboard(scoreboard);
    }

    /**
     * Updates the Title of the scoreboard.
     * @param player Player to update.
     * @param string String to update to.
     */
    public void updateScoreboardTitle(Player player, String string) {
        Scoreboard scoreboard = getScoreboard(player);
        if (scoreboard == null) {
            return;
        }
        Objective objective = scoreboard.getObjective("Identifier");
        if (objective == null) {
            return;
        }
        objective.setDisplayName(TextUtil.parseColoredString(string));
    }

    /**
     * Gets the scoreboard that is currently cached for the player.
     * @param player Player to get the scoreboard from.
     * @return Scoreboard for the player, null if one is not present.
     */
    @Nullable
    public Scoreboard getScoreboard(Player player) {
        return scoreboards.get(player.getName());
    }

    /**
     * Updates the scoreboard for a specific player. The scoreboard needs to be refreshed to be displayed again.
     * @param player Player to set scoreboard for.
     * @param scoreboard Scoreboard to set.
     */
    public void updateScoreboard(Player player, Scoreboard scoreboard) {
        scoreboards.remove(player.getName());
        scoreboards.put(player.getName(), scoreboard);
    }

    /**
     * Removes the scoreboard for the current player. Clears the scoreboard currently displayed to the player.
     * @param player Player to remove scoreboard for.
     */
    public void removeScoreboard(Player player) {
        scoreboards.remove(player.getName());
        player.setScoreboard(scoreboardManager.getNewScoreboard());
    }
}
