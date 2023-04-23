package mrkeith782.bedwars.Commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.Util.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArmorStandCommand implements CommandExecutor {
    private final Bedwars bedwars = Bedwars.getInstance();
    private final TextUtil textutil = new TextUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { //all of my commands atm are for players so i'm happy with this
            return false;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("placearmorstand")) {
            if (args.length == 0) {
                return false;
            }

            //no real reason to do this atm, but it's what it'll look like in the future
            TextComponent coloredDisplayName = textutil.parseColoredString(args[1]);
            bedwars.getAsm().spawnNewArmorStand(player.getLocation(), coloredDisplayName, args[0]);

            player.spigot().sendMessage(new TextComponent("Spawned an armor stand with name " + coloredDisplayName.getText() + "."));
            return true;
        }

        if (command.getName().equalsIgnoreCase("editarmorstand")) {
            if (args.length == 0) {
                return false;
            }

            TextComponent coloredDisplayName = textutil.parseColoredString(args[1]);
            boolean success = bedwars.getAsm().editArmorStandDisplay(args[0], coloredDisplayName);
            if (success) {
                player.spigot().sendMessage(new TextComponent("Edited an armor stand to " + coloredDisplayName.getText() + "."));
                return true;
            }
            else {
                player.spigot().sendMessage(new TextComponent("Could not find an armor stand with ID " + args[0] + "."));
                return false;
            }
        }

        if (command.getName().equalsIgnoreCase("printarmorstands")) {
            bedwars.getAsm().printAllArmorStands();
            return true;
        }
        return false;
    }
}
