package mrkeith782.bedwars.managers;

import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;

import java.util.HashMap;
import java.util.Map;

public class ArmorStandManager {
    private final Map<String, ArmorStand> holograms = new HashMap<>();
    private final Map<String, TextDisplay> displays = new HashMap<>();

    /**
     * Spawns and stores the ID of an Armor Stand.
     * @param location Location to spawn the armor stand at.
     * @param name Value to display on the armor stand. Parses color codes.
     * @param ID ID of the armor stand
     */
    public void spawnNewArmorStand(Location location, String name, String ID) {
        if (location.getWorld() == null) {
            return;
        }
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
        as.setSmall(true);
        as.setMarker(true);
        as.setCustomName(TextUtil.parseColoredString(name));
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
     * Spawns and stores the ID of a TextDisplay.
     * @param location Location to spawn at
     * @param name Value to display. Parses colors.
     * @param ID ID of the Display
     * @param pitch Orientation at which to show this display. 0 faces south, 90 west, 180 north, 270 east.
     */
    public void spawnNewTextDisplay(Location location, String name, String ID, int pitch) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        TextDisplay textDisplay = (TextDisplay) world.spawnEntity(location, EntityType.TEXT_DISPLAY);
        textDisplay.setText(TextUtil.parseColoredString(name));
        textDisplay.setCustomNameVisible(true);
        textDisplay.setRotation(pitch, 0);

        displays.put(ID, textDisplay);
    }

    /**
     * Edits a TextDisplay's text by ID.
     * @param ID ID of the display to edit
     * @param name New text to display
     * @return True if the display could be found, false if not
     */
    public boolean editTextDisplay(String ID, String name) {
        TextDisplay textDisplay = displays.get(ID);
        if (textDisplay == null) {
            return false;
        }

        textDisplay.setText(TextUtil.parseColoredString(name));
        return true;
    }

    /**
     * Removes a TextDisplay by ID.
     * @param ID ID of TextDisplay to remove.
     * @return True if it could be removed, false if not.
     */
    public boolean removeTextDisplay(String ID) {
        TextDisplay textDisplay = displays.get(ID);
        if (textDisplay == null) {
            return false;
        }

        textDisplay.remove();
        displays.remove(ID);
        return true;
    }

    /**
     * Removes all TextDisplays stored in the ASM.
     */
    public void removeAllTextDisplays() {
        for(String key : displays.keySet()) {
            removeTextDisplay(key);
        }
    }
}
