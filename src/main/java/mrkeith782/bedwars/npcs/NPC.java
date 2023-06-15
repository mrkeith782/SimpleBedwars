package mrkeith782.bedwars.npcs;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public abstract class NPC {
    @Getter
    String npcID;
    @Getter
    String npcName;
    @Getter
    Entity entity;
    // Determines which direction the NPC is default looking at, when no other player is around
    int yaw;

    public abstract void handleClick(PlayerInteractEntityEvent event);

    public abstract void createEntity(Location location);
}
