package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.npcs.NPC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCManager {
    Map<String, Entity> npcs = new HashMap<>();
    List<NPC> npcClassList = new ArrayList<>();

    /**
     * Store an NPC in the NPCManager, and spawn it
     * @param npc NPC to store
     * @param location Location to spawn the NPC
     */
    public void spawnAndStoreNPC(NPC npc, Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        npc.createEntity(location);
        npcs.put(npc.getNpcID(), npc.getEntity());
        npcClassList.add(npc);
    }

    /**
     * Remove all NPCs that are currently registered with the NPC Manager
     */
    public void removeAllNPCs() {
        for (Entity entity : npcs.values()) {
            entity.remove();
        }
        npcs.clear();
        npcClassList.clear();
    }

    public List<NPC> getNpcList() {
        return npcClassList;
    }
}
