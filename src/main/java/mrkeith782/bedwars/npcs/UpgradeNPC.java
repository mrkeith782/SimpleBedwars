package mrkeith782.bedwars.npcs;

import mrkeith782.bedwars.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class UpgradeNPC extends NPC {

    public UpgradeNPC(int yaw) {
        this.npcID = "UPGRADE_NPC";
        this.npcName = "Upgrade Shop";
        this.yaw = yaw;
    }

    @Override
    public void handleClick(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();

        Bedwars.getInstance().getBedwarsGame().getMenuManager().openMenu("UPGRADE_MENU", player);
        Bukkit.broadcastMessage("OPENED UPGRADE_MENU");
    }

    @Override
    public void createEntity(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        location.setYaw(this.yaw);
        LivingEntity npc = (LivingEntity) world.spawnEntity(location, EntityType.VILLAGER);
        npc.setAI(false);
        npc.setSilent(true);
        npc.setInvulnerable(true);
        npc.setCollidable(false);
        npc.setGravity(false);

        this.entity = npc;
    }
}
