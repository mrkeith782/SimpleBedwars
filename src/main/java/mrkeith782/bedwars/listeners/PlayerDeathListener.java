package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.game.BedwarsTeam;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeathListener(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null || !game.contains(victim)) {
            return;
        }

        BedwarsTeam team = game.getTeamByPlayer(victim);
        if (team == null) {
            return;
        }

        if (killer == null) { //This means there was not a killer involved with the player's death
            game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%gray%%") + event.getDeathMessage());
        }

        event.setDeathMessage(null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> victim.spigot().respawn());
    }

    @EventHandler
    public void onPlayerRespawnListener(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null) {
            return;
        }

        BedwarsTeam bedwarsTeam = game.getTeamByPlayer(player);
        if (bedwarsTeam == null) {
            return;
        }

        event.setRespawnLocation(bedwarsTeam.getTeamGeneratorLocation());
    }
}
