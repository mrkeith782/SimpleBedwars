package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.npcs.NPC;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCManager {
    //I do the map this way because there are lots of duplicate String IDs, but not Entities!
    final Map<Entity, String> npcs = new HashMap<>();
    final List<NPC> npcClassList = new ArrayList<>();
    BukkitTask npcLook;

    /**
     * Store an NPC in the NPCManager, and spawn it
     * @param npc NPC to store
     * @param location Location to spawn the NPC
     */
    /**
     * Spawn a brand new NPC and store it in the NPC Manager.
     * @param npc NPC to spawn
     * @param location Location to spawn the NPC
     */
    public void spawnAndStoreNPC(NPC npc, Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        npc.createEntity(location);
        npcs.put(npc.getEntity(), npc.getNpcID());
        npcClassList.add(npc);
    }

    /**
     * Remove all NPCs and Entities that are currently registered with the NPC Manager.
     */
    /**
     * Remove all NPCs that are currently registered with the NPC Manager
     */
    public void removeAllNPCs() {
        for (Entity entity : npcs.keySet()) {
            entity.remove();
        }
        npcs.clear();
        npcClassList.clear();
    }

    /**
     * Creates and runs the task that has NPCs look at nearby players.
     */
    public void buildNpcLookTask() {
        this.npcLook = new BukkitRunnable() {
            Level level;
            @Override
            public void run() {
                // Let's go through all of our entities in the NPC Manager
                for (Entity entity : npcs.keySet()) {
                    if (level == null) {
                        CraftWorld world = (CraftWorld) entity.getWorld();
                        level = world.getHandle();
                    }

                    net.minecraft.world.entity.Entity nmsEntity = level.getEntity(entity.getEntityId());
                    if (nmsEntity == null) {
                        continue;
                    }

                    // Let's see if we have a player nearby, and then have our NPC look at the player
                    List<Entity> nearbyEntities = entity.getNearbyEntities(3, 3, 3);
                    for (Entity nearbyEntity : nearbyEntities) {
                        if (!(nearbyEntity instanceof Player)) {
                            continue;
                        }

                        // Get difference between NPC's location and the player's
                        CraftPlayer craftPlayer = (CraftPlayer) nearbyEntity;

                        Location playerLoc = craftPlayer.getLocation();
                        Location entityLoc = entity.getLocation();

                        Vector locDiff = playerLoc.toVector().subtract(entityLoc.toVector()).normalize();

                        // Have the NPC pitch change to face the player
                        ClientboundMoveEntityPacket.Rot packet1 = new ClientboundMoveEntityPacket.Rot(
                                nmsEntity.getId(),
                                (byte) 0,
                                (byte) ((Math.asin(-locDiff.getY()) * (256.0F / 360.0F)) * 75),
                                false
                        );

                        // Have the NPC yaw change to face the player
                        ClientboundRotateHeadPacket packet2 = new ClientboundRotateHeadPacket(
                                nmsEntity,
                                (byte) (-(Math.atan2(locDiff.getX(), locDiff.getZ()) * (256.0F / 360.0F)) * 60)
                        );

                        craftPlayer.getHandle().connection.send(packet1);
                        craftPlayer.getHandle().connection.send(packet2);
                    }
                }
            }
        }.runTaskTimer(Bedwars.getInstance(), 0, 2L);
    }

    /**
     * Stop NPCs from looking at players. Should be called before the game ends.
     */
    public void stopNpcLookTask() {
        this.npcLook.cancel();
    }

    public List<NPC> getNpcList() {
        return npcClassList;
    }
}
