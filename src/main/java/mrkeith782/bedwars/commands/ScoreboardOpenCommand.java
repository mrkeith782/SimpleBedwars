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

            //This is so we don't have to repeat this logic infinitely
            final Map<Integer, String> lineCache = new HashMap<>();

            @Override
            public void run() {
                loopTime++;
                StringBuilder bw = new StringBuilder("    ");

                if (loopTime >= 0 && loopTime < 10) { //YELLOW MODE
                    bw.append(ChatColor.of(bedwarsColor)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 10 && loopTime < 100) { //MOVE ORANGE THROUGH TEXT
                    String cachedLine = lineCache.get(loopTime);
                    if (cachedLine != null) {
                        bw.append(cachedLine);
                    } else {
                        int orangeTime = (loopTime - 10) * 2;

                        //We could hypothetically make this more efficient by dropping the colors and using purely hex codes in chat, but that's for a less angry keith
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
                } else if (loopTime >= 100 && loopTime < 120) {
                    bw.append(ChatColor.of(bedwarsColor)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 120 && loopTime < 140) {
                    bw.append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
                } else if (loopTime >= 140 && loopTime < 180) {
                    bw.append(ChatColor.of(bedwarsColor)).append(TextUtil.parseColoredString("%%bold%%BEDWARS"));
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
