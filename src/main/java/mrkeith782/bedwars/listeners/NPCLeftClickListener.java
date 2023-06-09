package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.npcs.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCLeftClickListener implements Listener {
    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event) {
        //We're only worried about clicks in the game
        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null) {
            return;
        }

        for (NPC npc : game.getNpcManager().getNpcList()) {
            //todo: range check?
            if (npc.getEntity().getUniqueId() == event.getRightClicked().getUniqueId()) {
                npc.handleClick(event);
            }
        }
    }
}
