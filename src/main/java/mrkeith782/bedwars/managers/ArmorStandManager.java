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
import java.util.logging.Level;

public class ArmorStandManager {
    private final Map<String, ArmorStand> holograms = new HashMap<>();
    private final Map<String, TextDisplay> displays = new HashMap<>();

    /**
     * Spawns and stores the ID of an Armor Stand.
     *
     * @param location Location to spawn the armor stand at.
     * @param name     Value to display on the armor stand. Parses color codes.
     * @param ID       ID of the armor stand
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
     *
     * @param ID ID to edit
     * @param name New value to display
     */
    public void editArmorStandDisplay(String ID, String name) {
        ArmorStand as = holograms.get(ID);
        if (as == null) {
            Bukkit.getLogger().log(Level.WARNING, "Called editArmorStandDisplay with a hologram that doesn't exist!");
            return;
        }

        as.setCustomName(TextUtil.parseColoredString(name));
    }

    /**
     * Remove and despawn an armor stand that is currently stored
     *
     * @param ID ID of the armor stand to remove
     */
    public void removeArmorStand(String ID) {
        ArmorStand as = holograms.get(ID);
        if (as == null) {
            Bukkit.getLogger().log(Level.WARNING, "Called removeArmorStand with a hologram that doesn't exist!");
            return;
        }

        as.remove();
        holograms.remove(ID);
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
     *
     * @param location Location to spawn at
     * @param name     Value to display. Parses colors.
     * @param ID       ID of the Display
     * @param yaw      Orientation at which to show this display. 0 faces south, 90 west, 180 north, 270 east.
     */
    public void spawnNewTextDisplay(Location location, String name, String ID, int yaw) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        TextDisplay textDisplay = (TextDisplay) world.spawnEntity(location, EntityType.TEXT_DISPLAY);
        textDisplay.setText(TextUtil.parseColoredString(name));
        textDisplay.setCustomNameVisible(true);
        textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
        textDisplay.setRotation(yaw, 0);

        displays.put(ID, textDisplay);
    }

    /**
     * Edits a TextDisplay's text by ID.
     *
     * @param ID   ID of the display to edit
     * @param name New text to display
     */
    public void editTextDisplay(String ID, String name) {
        TextDisplay textDisplay = displays.get(ID);
        if (textDisplay == null) {
            Bukkit.getLogger().log(Level.WARNING, "Called editTextDisplay with a TextDisplay that doesn't exist!");
            return;
        }

        textDisplay.setText(TextUtil.parseColoredString(name));
    }

    /**
     * Removes a TextDisplay by ID.
     *
     * @param ID ID of TextDisplay to remove.
     */
    public void removeTextDisplay(String ID) {
        TextDisplay textDisplay = displays.get(ID);
        if (textDisplay == null) {
            Bukkit.getLogger().log(Level.WARNING, "Called removeTextDisplay with a TextDisplay that doesn't exist!");
            return;
        }

        textDisplay.remove();
        displays.remove(ID);
    }

    /**
     * Removes all TextDisplays stored in the ASM.
     */
    public void removeAllTextDisplays() {
        for (TextDisplay textDisplay : displays.values()) {
            textDisplay.remove();
        }
    }
}
