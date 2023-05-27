package mrkeith782.bedwars.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;

public class ArmorStandManager {
    private final Map<String, ArmorStand> holograms = new HashMap<>();

    /**
     * Spawns and stores the ID of an Armor Stand. Does not parse color codes.
     * @param location Location to spawn the armor stand at.
     * @param name Value to display on the armor stand
     * @param ID ID of the armor stand
     */
    public void spawnNewArmorStand(Location location, String name, String ID) {
        if (location.getWorld() == null) {
            return;
        }
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
        as.setSmall(true);
        as.setMarker(true);
        as.setCustomName(name);
        as.setCustomNameVisible(true);
        as.setVisible(false);
        holograms.put(ID, as);
    }

    /**
     * Edit an armor stand's display by ID
     * @param ID ID to edit
     * @param name New value to display
     * @return True if the armor stand could be found, false if not.
     */
    public boolean editArmorStandDisplay(String ID, String name) {
        ArmorStand as = holograms.get(ID);
        if (as == null) {
            return false;
        }

        as.setCustomName(name);
        return true;
    }

    /**
     * Remove and despawn an armor stand that is currently stored
     * @param ID ID of the armor stand to remove
     * @return True if it could be removed, false if not found.
     */
    public boolean removeArmorStand(String ID) {
        ArmorStand as = holograms.get(ID);
        if (as == null) {
            return false;
        }

        as.remove();
        holograms.remove(ID);
        return true;
    }

    /**
     * Remove all currently stored armor stands in the ASM.
     */
    public void removeAllArmorStands() {
        for (ArmorStand as : holograms.values()) {
            as.remove();
        }
    }

    /**
     * Print all currently stored armor stands in the ASM.
     */
    public void printAllArmorStands() {
        Bukkit.broadcastMessage("PRINTING ALL ARMOR STANDS:");
        Bukkit.broadcastMessage("ID | NAME");
        for (String key : holograms.keySet()) {
            String hologramDisplay = holograms.get(key).getName();
            Bukkit.broadcastMessage(key + " " + hologramDisplay);
        }
    }
}
