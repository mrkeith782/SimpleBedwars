package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedwarsCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) { //Base command.
            sender.sendMessage("Commands:");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            Bedwars.getInstance().createNewGame();
            return true;
        }

        if (args[0].equalsIgnoreCase("join")) {
            player.sendMessage(TextUtil.parseColoredString("%%yellow%%Sending you to the game..."));
            if (Bedwars.getInstance().getBedwarsGame().addBedwarsPlayer(player)) {
                player.sendMessage(TextUtil.parseColoredString("%%yellow%%Welcome!"));
                return true;
            }
            //TODO: Get failure reason
            player.sendMessage(TextUtil.parseColoredString("%%red%%Failed sending you to the Bedwars game. Please try again later!"));
            return true;
        }

        return false;
    }
}
