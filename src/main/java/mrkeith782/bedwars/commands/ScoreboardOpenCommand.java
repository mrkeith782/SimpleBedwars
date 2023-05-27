package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.BedwarsScoreboardManager;
import mrkeith782.bedwars.util.TextUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ScoreboardOpenCommand implements CommandExecutor {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final BedwarsScoreboardManager scoreboardManager = bedwars.getSbm();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { //all of my commands atm are for players so i'm happy with this
            return false;
        }

        Player player = (Player) sender;

        List<String> scoreboardLines = new ArrayList<>();
        scoreboardLines.add("");
        scoreboardLines.add("a");
        scoreboardLines.add("b");
        scoreboardLines.add("c");

        scoreboardManager.createNewScoreboard(player, scoreboardLines);
        scoreboardManager.refreshScoreboard(player);

        //TODO: Refactor so that this logic runs only once for all players, not once per player.
        BukkitRunnable bedwarsColors = new BukkitRunnable() {
            //Gets where we are in the color display. Since there are multiple stages, this is the most elegant way to do it.
            int loopTime = 0;
            final Color yellow = new Color(255, 255, 0);

            //If we've already created a line before, the runnable will attempt to set / get it here
            final Map<Integer, String> lineCache = new HashMap<>();

            @Override
            public void run() {
                loopTime++;
                StringBuilder bw = new StringBuilder("    ");

                if (loopTime >= 0 && loopTime < 10) { //Display yellow text
                    bw.append(ChatColor.of(yellow)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 10 && loopTime < 100) { //Display orange moving through yellow text
                    String cachedLine = lineCache.get(loopTime);
                    if (cachedLine != null) {
                        bw.append(cachedLine);
                    } else {
                        int orangeTime = (loopTime - 10) * 2;

                        /*
                            Okay idk what crack I was on when I made this but we're incrementing the color to
                            make it more orange until we reach a specific rgb value, where we'll set the color to white.
                         */

                        //todo: refactor into arraylist?
                        Map<String, Color> colorMap = new LinkedHashMap<>();
                        int temp = (int) (255 - orangeTime * 1.25);
                        colorMap.put(TextUtil.parseColoredString("%%bold%%B"), new Color(255, temp >= 100 ? temp : 255, temp >= 100 ? 0 : 255));
                        temp = (int) (255 - orangeTime * 1.20);
                        colorMap.put(TextUtil.parseColoredString("%%bold%%E"), new Color(255, temp >= 100 ? temp : 255, temp >= 100 ? 0 : 255));
                        temp = (int) (255 - orangeTime * 1.15);
                        colorMap.put(TextUtil.parseColoredString("%%bold%%D"), new Color(255, temp >= 100 ? temp : 255, temp >= 100 ? 0 : 255));
                        temp = (int) (255 - orangeTime * 1.10);
                        colorMap.put(TextUtil.parseColoredString("%%bold%%W"), new Color(255, temp >= 100 ? temp : 255, temp >= 100 ? 0 : 255));
                        temp = (int) (255 - orangeTime * 1.05);
                        colorMap.put(TextUtil.parseColoredString("%%bold%%A"), new Color(255, temp >= 100 ? temp : 255, temp >= 100 ? 0 : 255));
                        temp = (int) (255 - orangeTime * 1.00);
                        colorMap.put(TextUtil.parseColoredString("%%bold%%R"), new Color(255, temp >= 100 ? temp : 255, temp >= 100 ? 0 : 255));
                        temp = (int) (255 - orangeTime * 0.95);
                        colorMap.put(TextUtil.parseColoredString("%%bold%%S"), new Color(255, temp >= 100 ? temp : 255, temp >= 100 ? 0 : 255));

                        StringBuilder completedLine = new StringBuilder();
                        for (String string : colorMap.keySet()) {
                            completedLine.append(ChatColor.of(colorMap.get(string))).append(string);
                        }

                        bw.append(completedLine);
                        lineCache.put(loopTime, completedLine.toString());
                    }
                } else if (loopTime >= 100 && loopTime < 120) { //Flash white and orange, and then reset.
                    bw.append(ChatColor.of(yellow)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 120 && loopTime < 140) {
                    bw.append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 140 && loopTime < 180) {
                    bw.append(ChatColor.of(yellow)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime == 180) { //Complete the loop, reset to first stage.
                    loopTime = 0;
                }
                bw.append("   ");

                scoreboardManager.updateScoreboardTitle(player, bw.toString());
                scoreboardManager.refreshScoreboard(player);
            }
        };

        bedwarsColors.runTaskTimer(bedwars, 1L, 1L);
        return true;
    }
}
