package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.Bedwars;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneratorManager {
    Map<String, Location> generatorLocations;
    List<Entity> generatorArmorStands = new ArrayList<>();
    BukkitTask generatorLoop;

    public void addNewGenerator(String id, Location location) {
        generatorLocations.put(id, location);
    }

    public Map<String, Location> getGeneratorLocations() {
        return generatorLocations;
    }

    public void placeGenerators() {
        for (String string : generatorLocations.keySet()) {
            Location location = generatorLocations.get(string);
            World world = location.getWorld();
            if (world == null) {
                continue;
            }

            ArmorStand as = world.spawn(location, ArmorStand.class);
            EntityEquipment eq = as.getEquipment();
            if (eq == null) {
                continue;
            }

            if (string.toLowerCase().contains("diamond")) {
                eq.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
            } else if (string.toLowerCase().contains("emerald")) {
                eq.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
            }
            as.setVisible(false);

            generatorArmorStands.add(as);
        }
    }

    public void startRotation() {
        this.generatorLoop = new BukkitRunnable() {
            int yaw = 0;
            int pitch = 0;
            @Override
            public void run() {
                for (Entity entity : generatorArmorStands) {
                    entity.setRotation(0, 0);
                    yaw++;
                    pitch++;
                }
            }
        }.runTaskTimer(Bedwars.getInstance(), 0L, 1L);
    }

    public void stopRotation() {
        this.generatorLoop.cancel();
    }

    public void removeAllGenerators() {
        for (Entity entity : generatorArmorStands) {
            entity.remove();
        }
        generatorLocations = null;
    }
}
