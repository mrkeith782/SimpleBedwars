package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.GameStatus;
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
            player.sendMessage(TextUtil.parseColoredString("%%yellow%%Currently building the game..."));
            if (Bedwars.getInstance().getBedwarsGame() != null) {
                GameStatus currentStatus = Bedwars.getInstance().getBedwarsGame().getGameStatus();
                switch (currentStatus) {
                    case BUILDING:
                        player.sendMessage(TextUtil.parseColoredString("%%red%%Failed! %%yellow%%The game is currently building..."));
                        return true;
                    case PREGAME:
                        player.sendMessage(TextUtil.parseColoredString("%%red%%Failed! %%yellow%%There already is a game! You can use %%green%%/bedwars join%%yellow%% to join."));
                        return true;
                    default:
                        player.sendMessage(TextUtil.parseColoredString("%%red%%Failed! %%yellow%%There is currently a game in progress!"));
                        return true;
                }
            }

            Bedwars.getInstance().createNewGame();
            if (Bedwars.getInstance().getBedwarsGame().getGameStatus() == GameStatus.FAILED) {
                player.sendMessage(TextUtil.parseColoredString("%%red%%Failed! %%yellow%%You can try again later."));
                return true;
            }

            player.sendMessage(TextUtil.parseColoredString("%%yellow%%Finished! You can now join the game with %%green%%/bedwars join%%yellow%%."));
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

        if (args[0].equalsIgnoreCase("start")) {
            if (Bedwars.getInstance().getBedwarsGame().getGameStatus() != GameStatus.PREGAME) {
                player.sendMessage(TextUtil.parseColoredString("%%yellow%%It appears as if there is not a game that can be started. Please use %%green%%/bedwars create%%yellow%%."));
            }
            player.sendMessage(TextUtil.parseColoredString("%%yellow%%Attempting to start the game..."));
            Bedwars.getInstance().getBedwarsGame().startGame();
            return true;
        }

        if (args[0].equalsIgnoreCase("forceend")) {
            player.sendMessage(TextUtil.parseColoredString("%%yellow%%Force ending the game..."));
            Bedwars.getInstance().cleanGame();
            return true;
        }

        return false;
    }
}
