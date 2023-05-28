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

    public void spawnAndStoreNPC(NPC npc, Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        npc.createEntity(location);
        npcs.put(npc.getNpcID(), npc.getEntity());
        npcClassList.add(npc);
    }

    public List<NPC> getNpcList() {
        return npcClassList;
    }

    public void removeAllNPCs() {
        for (Entity entity : npcs.values()) {
            entity.remove();
        }
        npcs.clear();
        npcClassList.clear();
    }
}
