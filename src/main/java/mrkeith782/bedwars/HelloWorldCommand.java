package mrkeith782.bedwars;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class HelloWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            TextManagerUtil tmu = new TextManagerUtil();
            TextComponent baseString = tmu.parseColoredString("%%gray%%" + player + " executed a command!");
            TextComponent hoverString = tmu.parseColoredString("%%green%%Nerds!");
            TextComponent sendString = tmu.addHoverText(baseString, hoverString);

            player.spigot().sendMessage(sendString);
            return true;
        }
        return false;
    }
}
