package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

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

    public void refreshScoreboard(Player player) {
        Scoreboard scoreboard = getScoreboard(player);
        if (scoreboard == null) return;
        player.setScoreboard(scoreboard);
    }

    public void updateScoreboardTitle(Player player, String string) {
        Scoreboard scoreboard = getScoreboard(player);
        if (scoreboard == null) return;
        Objective objective = scoreboard.getObjective("Identifier");
        if (objective == null) return;
        objective.setDisplayName(string);
    }

    public Scoreboard getScoreboard(Player player) {
        return scoreboards.get(player.getName());
    }

    public void updateScoreboard(Player player, Scoreboard scoreboard) {
        scoreboards.remove(player.getName());
        scoreboards.put(player.getName(), scoreboard);
    }

    public void removeScoreboard(Player player) {
        scoreboards.remove(player.getName());
        player.setScoreboard(scoreboardManager.getNewScoreboard());
    }
}
