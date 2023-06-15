package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamDamageListener implements Listener {

    @EventHandler
    public void onTeamDamageListener(EntityDamageByEntityEvent event) {
        // We're only cancelling team damage, both need to be players for this to occur
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        // Make sure we're in a game
        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (game.getTeamByPlayer(victim) == game.getTeamByPlayer(damager)) {
            event.setCancelled(true);
        }
    }
}
