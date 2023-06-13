package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.npcs.NPC;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class NPCManager {
    //I do the map this way because there are lots of duplicate String IDs, but not Entities!
    final Map<Entity, String> npcs = new HashMap<>();
    final List<NPC> npcClassList = new ArrayList<>();
    BukkitTask npcLook;

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
     * Get the NPCs that are currently registered with the NPC Manager.
     * @return List of NPCs currently registered
     */
    public List<NPC> getNpcList() {
        return npcClassList;
    }

    /**
     * Remove all NPCs and Entities that are currently registered with the NPC Manager.
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

                for (Entity entity : npcs.keySet()) {
                    if (level == null) {
                        CraftWorld world = (CraftWorld) entity.getWorld();
                        level = world.getHandle();
                    }

                    net.minecraft.world.entity.Entity nmsEntity = level.getEntity(entity.getEntityId());
                    if (nmsEntity == null) {
                        continue;
                    }

                    for (Entity nearbyEntity : entity.getNearbyEntities(3, 3,3)) {
                        if (!(nearbyEntity instanceof Player)) {
                            continue;
                        }

                        CraftPlayer craftPlayer = (CraftPlayer) nearbyEntity;

                        Location playerLoc = craftPlayer.getLocation();
                        Location entityLoc = entity.getLocation();

                        Vector locDiff = playerLoc.toVector().subtract(entityLoc.toVector()).normalize();

                        ClientboundMoveEntityPacket.Rot packet1 = new ClientboundMoveEntityPacket.Rot(
                                nmsEntity.getId(),
                                (byte) 0,
                                (byte) ((Math.asin(-locDiff.getY()) * (256.0F / 360.0F)) * 75),
                                false
                        );

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
}
