package mrkeith782.bedwars.listeners;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.game.BedwarsGame;
import mrkeith782.bedwars.game.BedwarsTeam;
import mrkeith782.bedwars.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeamChestListener implements Listener {

    @EventHandler
    public void onTeamChestListener(PlayerInteractEvent event) {
        // We're only worried about clicks in the game
        BedwarsGame game = Bedwars.getInstance().getBedwarsGame();
        if (game == null) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        BedwarsTeam bedwarsTeam = game.getTeamByPlayer(event.getPlayer());
        if (bedwarsTeam == null) {
            return;
        }

        if (event.getClickedBlock().getBlockData().getMaterial() == Material.CHEST && !event.getClickedBlock().getLocation().equals(bedwarsTeam.getChestLocation())) {
            event.getPlayer().sendMessage(TextUtil.parseColoredString("%%red%%You cannot open that chest until that team is dead!"));
            event.setCancelled(true);
        }
    }
}
