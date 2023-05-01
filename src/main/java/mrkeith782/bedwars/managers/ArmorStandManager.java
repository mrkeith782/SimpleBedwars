package mrkeith782.bedwars.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;

public class ArmorStandManager {
    private final Map<String, ArmorStand> holograms = new HashMap<>();

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

    public boolean editArmorStandDisplay(String ID, String name) {
        ArmorStand as = holograms.get(ID);
        if (as == null) {
            return false;
        }

        as.setCustomName(name);
        return true;
    }

    public boolean removeArmorStand(String ID) {
        ArmorStand as = holograms.get(ID);
        if (as == null) {
            return false;
        }

        as.remove();
        return true;
    }

    public void removeAllArmorStands() {
        for (ArmorStand as : holograms.values()) {
            as.remove();
        }
    }

    public void printAllArmorStands() {
        Bukkit.broadcastMessage("PRINTING ALL ARMOR STANDS:");
        Bukkit.broadcastMessage("ID | NAME");
        for (String key : holograms.keySet()) {
            String hologramDisplay = holograms.get(key).getName();
            Bukkit.broadcastMessage(key + " " + hologramDisplay);
        }
    }
}