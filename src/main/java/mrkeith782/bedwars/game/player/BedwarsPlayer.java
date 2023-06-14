package mrkeith782.bedwars.game.player;

import mrkeith782.bedwars.game.team.BedwarsTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BedwarsPlayer {
    UUID playerUUID;
    PlayerStatus status;
    BedwarsTeam team;
    int kills;
    int bedsBroken;
    int finalKills;
    boolean needsUpdate = false;

    public BedwarsPlayer(Player player) {
        this.playerUUID = player.getUniqueId();
        this.status = PlayerStatus.PREGAME;
        this.kills = 0;
    }

    public void incKills() {
        this.kills++;
        this.needsUpdate = true;
    }

    public void incFinalKills() {
        this.finalKills++;
        this.needsUpdate = true;
    }

    public void incBedsBroken() {
        this.bedsBroken++;
        this.needsUpdate = true;
    }

    public void setNeedsUpdate(boolean update) {
        this.needsUpdate = update;
    }

    public boolean getNeedsUpdate() {
        return needsUpdate;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public BedwarsTeam getTeam() {
        return team;
    }

    public void setTeam(BedwarsTeam team) {
        this.team = team;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.getPlayerUUID());
    }
    public int getKills() {
        return this.kills;
    }

    public int getFinalKills() {
        return this.finalKills;
    }
    public int getBedsBroken() {
        return this.bedsBroken;
    }
}
