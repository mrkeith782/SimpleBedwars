package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.*;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeathListener(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        // Check if we have a game, and if the player is in a team in the game
        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null || !game.contains(victim)) {
            return;
        }

        BedwarsTeam team = game.getTeamByPlayer(victim);
        if (team == null) {
            return;
        }

        // This means there was not a killer involved with the player's death
        if (killer == null) {
            game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%gray%%") + event.getDeathMessage());
        } else {
            BedwarsPlayer bedwarsKiller = game.getBedwarsPlayer(killer);
            BedwarsPlayer bedwarsVictim = game.getBedwarsPlayer(victim);
            if (bedwarsKiller == null || bedwarsVictim == null) {
                return;
            }

            BedwarsTeam victimTeam = bedwarsVictim.getTeam();
            if (victimTeam == null) {
                return;
            }

            // The victim gets stats for killing the player
            if (victimTeam.getTeamStatus() == TeamStatus.BED_BROKEN) {
                bedwarsKiller.incFinalKills();
            } else {
                bedwarsKiller.incKills();
            }
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

        BedwarsPlayer bedwarsPlayer = game.getBedwarsPlayer(player);
        if (bedwarsPlayer == null) {
            return;
        }

        player.setGameMode(GameMode.SPECTATOR);
        event.setRespawnLocation(bedwarsTeam.getTeamGeneratorLocation());

        if (!(bedwarsTeam.getTeamStatus() == TeamStatus.BED_BROKEN)) {
            // If the player is still alive, respawn them
            player.sendMessage(TextUtil.parseColoredString("%%yellow%%You will respawn in 5 seconds..."));
            bedwarsPlayer.setStatus(PlayerStatus.DEAD);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(bedwarsTeam.getTeamGeneratorLocation());
                bedwarsPlayer.setStatus(PlayerStatus.ALIVE);
            }, 100L);
        } else {
            // If the player's bed is broken, keep them dead
            player.sendMessage(TextUtil.parseColoredString("%%yellow%%You will not respawn because your Team's bed has been broken."));
            bedwarsPlayer.setStatus(PlayerStatus.FINAL_DEAD);
        }

    }
}
