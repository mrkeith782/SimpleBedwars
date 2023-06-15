package mrkeith782.bedwars.game.team;

import lombok.Getter;
import lombok.Setter;
import mrkeith782.bedwars.game.player.BedwarsPlayer;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class BedwarsTeam {
    @Getter
    String teamDisplayName;
    @Getter
    Color teamDisplayColor;
    @Getter
    Location bedLocation;
    @Getter
    Location chestLocation;
    @Getter
    Location enderChestLocation;
    @Getter
    Location teamGeneratorLocation;
    @Getter
    Location shopLocation;
    @Getter
    Location upgradesLocation;
    @Getter
    @Setter
    TeamStatus teamStatus;
    @Getter
    List<BedwarsPlayer> teamPlayers;
    @Setter
    boolean needsUpdate = false;

    public BedwarsTeam(String name, Color color, Location bedLoc, Location chestLoc, Location eChestLoc, Location teamGenLoc, Location shopLoc, Location upgradeLoc) {
        this.teamDisplayName = name;
        this.teamDisplayColor = color;
        this.bedLocation = bedLoc;
        this.chestLocation = chestLoc;
        this.enderChestLocation = eChestLoc;
        this.teamGeneratorLocation = teamGenLoc;
        this.shopLocation = shopLoc;
        this.upgradesLocation = upgradeLoc;
        teamPlayers = new ArrayList<>();
        teamStatus = TeamStatus.ALIVE;
    }

    public void addPlayerToTeam(BedwarsPlayer player) {
        teamPlayers.add(player);
    }

    public void removePlayerFromTeam(BedwarsPlayer player) {
        teamPlayers.remove(player);
    }

    public boolean getNeedsUpdate() {
        return this.needsUpdate;
    }
}
