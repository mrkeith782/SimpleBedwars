package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.game.BedwarsPlayer;
import mrkeith782.bedwars.game.BedwarsTeam;
import mrkeith782.bedwars.game.TeamStatus;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BedBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Make sure we actually have a game running
        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null) {
            return;
        }

        if (event.getBlock().getType() == Material.RED_BED) {
            event.setDropItems(false);


            BedwarsPlayer bedwarsPlayer = game.getBedwarsPlayer(event.getPlayer());

            if (bedwarsPlayer != null) {
                bedwarsPlayer.incBedsBroken();
            }

            //TODO: This is inherently fragile because the bed takes up two blocks worth of space
            for (BedwarsTeam team : game.getBedwarsTeams()) {
                if (team.getBedLocation().equals(event.getBlock().getLocation())) {
                    game.messageAllBedwarsPlayers(TextUtil.parseColoredString("%%yellow%%") + team.getTeamDisplayName() + "'s bed has been broken!");
                    team.setTeamStatus(TeamStatus.BED_BROKEN);
                    return;
                }
            }

            Bukkit.broadcastMessage("Bed broken without associated team?");
        }
    }
}
