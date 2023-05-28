package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.npcs.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCLeftClickListener implements Listener {
    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event) {
        for (NPC npc : Bedwars.getInstance().getNpcm().getNpcList()) {
            //todo: range check?
            if (npc.getEntity().getUniqueId() == event.getRightClicked().getUniqueId()) {
                npc.handleClick(event);
            }
        }
    }
}
