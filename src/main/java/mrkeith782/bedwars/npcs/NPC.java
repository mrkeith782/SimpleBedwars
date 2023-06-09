package mrkeith782.bedwars.npcs;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class NPC {
    String npcID;
    String npcName;
    Entity entity;
    //Determines which direction the NPC is looking at.
    int yaw;

    public abstract void handleClick(PlayerInteractEntityEvent event);

    public abstract void createEntity(Location location);

    public String getNpcID() {
        return npcID;
    }

    public String getNpcName() {
        return npcName;
    }

    public Entity getEntity() {
        return entity;
    }
}
