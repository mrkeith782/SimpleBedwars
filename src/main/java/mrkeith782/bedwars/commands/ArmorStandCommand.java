package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.util.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

public class ArmorStandCommand implements CommandExecutor {
    private final Bedwars bedwars = Bedwars.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { //all of my commands atm are for players so i'm happy with this
            return false;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("spawntextdisplay")) {
            bedwars.getAsm().spawnNewTextDisplay(player.getLocation(), "%%yellow%%%%bold%%abc", "temp", 90);
            return true;
        }

        if (command.getName().equalsIgnoreCase("placearmorstand")) {
            if (args.length == 0) {
                return false;
            }

            //no real reason to do this atm, but it's what it'll look like in the future
            String coloredDisplayName = TextUtil.parseColoredString(args[1]);
            bedwars.getAsm().spawnNewArmorStand(player.getLocation(), coloredDisplayName, args[0]);

            player.sendMessage("Spawned an armor stand with name " + coloredDisplayName + ".");
            return true;
        }

        if (command.getName().equalsIgnoreCase("editarmorstand")) {
            if (args.length == 0) {
                return false;
            }

            String coloredDisplayName = TextUtil.parseColoredString(args[1]);
            boolean success = bedwars.getAsm().editArmorStandDisplay(args[0], coloredDisplayName);
            if (success) {
                player.spigot().sendMessage(new TextComponent("Edited an armor stand to " + coloredDisplayName + "."));
                return true;
            }
            else {
                player.spigot().sendMessage(new TextComponent("Could not find an armor stand with ID " + args[0] + "."));
                return false;
            }
        }

        return false;
    }
}
