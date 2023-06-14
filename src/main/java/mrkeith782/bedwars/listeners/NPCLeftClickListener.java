package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.game.player.BedwarsPlayer;
import mrkeith782.bedwars.game.player.PlayerStatus;
import mrkeith782.bedwars.npcs.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCLeftClickListener implements Listener {
    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event) {
        // We're only worried about clicks in the game
        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null) {
            return;
        }

        //Make sure player is in game, and alive
        Player player = event.getPlayer();
        BedwarsPlayer bedwarsPlayer = game.getBedwarsPlayer(player);
        if (bedwarsPlayer == null || bedwarsPlayer.getStatus() != PlayerStatus.ALIVE) {
            return;
        }

        //Try to figure out which type of NPC the player clicked
        for (NPC npc : game.getNpcManager().getNpcList()) {
            if (npc.getEntity().getUniqueId() == event.getRightClicked().getUniqueId()) {
                npc.handleClick(event);
            }
        }
    }
}
