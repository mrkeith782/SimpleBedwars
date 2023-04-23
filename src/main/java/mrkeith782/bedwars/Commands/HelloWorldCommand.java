package mrkeith782.bedwars.Commands;

import mrkeith782.bedwars.Util.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelloWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            TextUtil tmu = new TextUtil();
            TextComponent baseString = tmu.parseColoredString("%%gray%%" + player.getDisplayName() + " executed a command!");
            TextComponent hoverString = tmu.parseColoredString("%%green%%Nerds!");
            TextComponent sendString = tmu.addHoverText(baseString, hoverString);

            player.spigot().sendMessage(sendString);
            return true;
        }
        return false;
    }
}
