package mrkeith782.bedwars.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BedwarsTeam {
    String teamColor;
    Location bedLocation;
    Location chestLocation;
    Location enderChestLocation;
    Location teamGeneratorLocation;
    Location shopLocation;
    Location upgradesLocation;
    List<UUID> teamPlayers;

    public BedwarsTeam(String color, Location bedLoc, Location chestLoc, Location eChestLoc, Location teamGenLoc, Location shopLoc, Location upgradeLoc) {
        this.teamColor = color;
        this.bedLocation = bedLoc;
        this.chestLocation = chestLoc;
        this.enderChestLocation = eChestLoc;
        this.teamGeneratorLocation = teamGenLoc;
        this.shopLocation = shopLoc;
        this.upgradesLocation = upgradeLoc;
        teamPlayers = new ArrayList<>();
    }

    public void addPlayerToTeam(BedwarsPlayer player) {
        teamPlayers.add(player.getPlayerUUID());
    }
}
