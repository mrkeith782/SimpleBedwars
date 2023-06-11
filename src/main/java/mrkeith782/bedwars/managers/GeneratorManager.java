package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.GameStatus;
import mrkeith782.bedwars.util.TextUtil;
import org.apache.logging.log4j.core.appender.ScriptAppenderSelector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GeneratorManager {
    final Map<String, Location> generatorLocations;
    final List<Entity> generatorArmorStands;
    BukkitTask generatorLoop;

    public GeneratorManager() {
        this.generatorArmorStands = new ArrayList<>();
        this.generatorLocations = new HashMap<>();
    }

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

            ArmorStandManager armorStandManager = Bedwars.getInstance().getBedwarsGame().getArmorStandManager();

            if (string.toLowerCase().contains("diamond")) {
                armorStandManager.spawnNewArmorStand(as.getLocation().clone().add(0, 3, 0), "%%yellow%%Tier %%red%%I", string + "_TIER");
                armorStandManager.spawnNewArmorStand(as.getLocation().clone().add(0, 2.7, 0), "%%aqua%%Diamond %%yellow%%Generator", string + "_NAME");
                armorStandManager.spawnNewArmorStand(as.getLocation().clone().add(0, 2.4, 0), "%%yellow%%Next material in %%green%%0:30", string + "_TIME");
                eq.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
            } else if (string.toLowerCase().contains("emerald")) {
                armorStandManager.spawnNewArmorStand(as.getLocation().clone().add(0, 3, 0), "%%yellow%%Tier %%red%%I", string + "_TIER");
                armorStandManager.spawnNewArmorStand(as.getLocation().clone().add(0, 2.7, 0), "%%green%%Emerald %%yellow%%Generator", string + "_NAME");
                armorStandManager.spawnNewArmorStand(as.getLocation().clone().add(0, 2.4, 0), "%%yellow%%Next material in %%green%%0:30", string + "_TIME");
                eq.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
            }
            as.setVisible(false);
            as.setGravity(false);
            as.setCollidable(false);

            generatorArmorStands.add(as);
        }
    }

    public void checkAndDropItems(int time) {
        GameStatus gameStatus = Bedwars.getInstance().getBedwarsGame().getGameStatus();
        ArmorStandManager armorStandManager = Bedwars.getInstance().getBedwarsGame().getArmorStandManager();
        int modifiedTime = 1800 - time;

        for (String string : generatorLocations.keySet()) {
            if (string.toLowerCase().contains("diamond")) { //This is an absolute shit way to do it, TODO: make a Generator class
                //Let's figure out if we should drop a diamond, and edit the armor stand for that
                int diamondTime;
                switch (gameStatus) {
                    case PHASE_1:
                    case PHASE_2:
                        diamondTime = 25;
                        break;
                    case PHASE_3:
                    case PHASE_4:
                    case PHASE_5:
                        diamondTime = 20;
                        break;
                    default:
                        diamondTime = 30;
                }
                armorStandManager.editArmorStandDisplay(string + "_TIME", "%%yellow%%Next material in %%green%%" + TextUtil.formatPrettyTime(modifiedTime % diamondTime));

                if (modifiedTime % diamondTime == 0) {
                    World world = generatorLocations.get(string).getWorld();
                    if (world == null) {
                        continue;
                    }
                    world.dropItem(generatorLocations.get(string), new ItemStack(Material.DIAMOND));
                }
            } else if (string.toLowerCase().contains("emerald")) {
                int emeraldTime;
                switch (gameStatus) {
                    case PHASE_1:
                    case PHASE_2:
                        emeraldTime = 35;
                        break;
                    case PHASE_3:
                    case PHASE_4:
                    case PHASE_5:
                        emeraldTime = 30;
                        break;
                    default:
                        emeraldTime = 45;
                }
                armorStandManager.editArmorStandDisplay(string + "_TIME", "%%yellow%%Next material in %%green%%" + TextUtil.formatPrettyTime(modifiedTime % emeraldTime));

                if (modifiedTime % emeraldTime == 0) {
                    World world = generatorLocations.get(string).getWorld();
                    if (world == null) {
                        continue;
                    }
                    world.dropItem(generatorLocations.get(string), new ItemStack(Material.EMERALD));
                }
            } else { //This will be the default island's generator.
                World world = generatorLocations.get(string).getWorld();
                if (world == null) {
                    continue;
                }

                //Let's get how many iron + gold we have near the generator, and stop generating if so
                Collection<Entity> entities = world.getNearbyEntities(generatorLocations.get(string), 3, 3, 3);
                int count = 0;
                for (Entity entity : entities) {
                    if (entity instanceof Item) {
                        Item item = (Item) entity;
                        if (item.getItemStack().getType() == Material.IRON_INGOT || item.getItemStack().getType() == Material.GOLD_INGOT) {
                            count++;
                        }
                    }
                }

                if (count >= 40) {
                    continue;
                }

                //Drop an extra iron / gold if there's not too many entities near the generator
                world.dropItem(generatorLocations.get(string), new ItemStack(Material.IRON_INGOT));
                if (modifiedTime % 7 == 0) {
                    world.dropItem(generatorLocations.get(string), new ItemStack(Material.GOLD_INGOT));
                }
            }
        }
    }

    public void startRotation() {
        this.generatorLoop = new BukkitRunnable() {
            int yaw = 0;

            @Override
            public void run() {
                for (Entity entity : generatorArmorStands) {
                    ArmorStand as = (ArmorStand) entity;
                    Location location = as.getLocation();
                    location.setYaw(yaw);

                    as.teleport(location);

                    yaw++;
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
    }
}
