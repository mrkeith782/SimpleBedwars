package mrkeith782.bedwars.game;

import org.bukkit.Color;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class BedwarsTeam {
    String teamDisplayName;
    Color teamDisplayColor;
    Location bedLocation;
    Location chestLocation;
    Location enderChestLocation;
    Location teamGeneratorLocation;
    Location shopLocation;
    Location upgradesLocation;
    TeamStatus teamStatus;
    List<BedwarsPlayer> teamPlayers;
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

    public List<BedwarsPlayer> getAllTeamPlayers() {
        return teamPlayers;
    }

    public Location getTeamGeneratorLocation() {
        return teamGeneratorLocation;
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public Location getEnderChestLocation() {
        return enderChestLocation;
    }

    public Location getChestLocation() {
        return chestLocation;
    }

    public Location getShopLocation() {
        return shopLocation;
    }

    public Location getUpgradesLocation() {
        return upgradesLocation;
    }

    public Color getTeamDisplayColor() {
        return teamDisplayColor;
    }

    public String getTeamDisplayName() {
        return teamDisplayName;
    }

    public void setTeamStatus(TeamStatus teamStatus) {
        this.teamStatus = teamStatus;
    }

    public TeamStatus getTeamStatus() {
        return teamStatus;
    }

    /**
     * Get if the team's scoreboard needs updated
     * @return True if it does, false if not
     */
    public boolean getNeedsUpdate() {
        return needsUpdate;
    }

    /**
     * If the team needs updated in the scoreboard, we flag it here
     * @param update true if it needs updated, false if not
     */
    public void setNeedsUpdate(boolean update) {
        this.needsUpdate = update;
    }
}
