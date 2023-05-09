package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.BedwarsScoreboardManager;
import mrkeith782.bedwars.managers.MenuManager;
import mrkeith782.bedwars.menus.Menu;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.awt.*;
import java.util.*;
import java.util.List;

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

        BukkitRunnable bedwarsColors = new BukkitRunnable() {
            int loopTime = 0;
            final Color bedwarsColor = new Color(255, 255, 0);

            /*
                Currmode can be
                YELLOW
                MOVE_ORANGE
                FLASH
             */

            @Override
            public void run() {
                loopTime++;
                StringBuilder bw = null;

                bw = new StringBuilder("    ");
                if (loopTime >= 0 && loopTime < 60) { //YELLOW MODE
                    bw.append(net.md_5.bungee.api.ChatColor.of(bedwarsColor)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 60 && loopTime < 180) { //MOVE ORANGE THROUGH TEXT
                    int orangeTime = (int) ((loopTime - 60) * 1.5);
                    LinkedHashMap<String, Color> colorMap = new LinkedHashMap<>();
                    colorMap.put(TextUtil.parseColoredString("%%bold%%B"), new Color(255, 195 - orangeTime >= 100 ? 195 - orangeTime : 255, 195 - orangeTime >= 100 ? 0 : 255));
                    colorMap.put(TextUtil.parseColoredString("%%bold%%E"), new Color(255, 205 - orangeTime >= 100 ? 205 - orangeTime : 255, 205 - orangeTime >= 100 ? 0 : 255));
                    colorMap.put(TextUtil.parseColoredString("%%bold%%D"), new Color(255, 215 - orangeTime >= 100 ? 215 - orangeTime : 255, 215 - orangeTime >= 100 ? 0 : 255));
                    colorMap.put(TextUtil.parseColoredString("%%bold%%W"), new Color(255, 225 - orangeTime >= 100 ? 225 - orangeTime : 255, 225 - orangeTime >= 100 ? 0 : 255));
                    colorMap.put(TextUtil.parseColoredString("%%bold%%A"), new Color(255, 235 - orangeTime >= 100 ? 235 - orangeTime : 255, 235 - orangeTime >= 100 ? 0 : 255));
                    colorMap.put(TextUtil.parseColoredString("%%bold%%R"), new Color(255, 245 - orangeTime >= 100 ? 245 - orangeTime : 255, 245 - orangeTime >= 100 ? 0 : 255));
                    colorMap.put(TextUtil.parseColoredString("%%bold%%S"), new Color(255, 255 - orangeTime >= 100 ? 255 - orangeTime : 255, 255 - orangeTime >= 100 ? 0 : 255));

                    for (String string : colorMap.keySet()) {
                        bw.append(net.md_5.bungee.api.ChatColor.of(colorMap.get(string))).append(string);
                    }
                } else if (loopTime >= 180 && loopTime < 195) {
                    bw.append(net.md_5.bungee.api.ChatColor.of(bedwarsColor)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 195 && loopTime < 210) {
                    bw.append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 210 && loopTime < 225) {
                    bw.append(net.md_5.bungee.api.ChatColor.of(bedwarsColor)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                    loopTime = 0;
                }
                bw.append("   ");

                scoreboardManager.updateScoreboardTitle(player, bw.toString());
                scoreboardManager.refreshScoreboard(player);
            }
        };

        bedwarsColors.runTaskTimerAsynchronously(bedwars, 1L, 1L);
        return true;
    }
}
