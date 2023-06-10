package mrkeith782.bedwars.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BedwarsPlayer {
    UUID playerUUID;
    PlayerStatus status;
    BedwarsTeam team;
    int deaths;
    int kills;

    public BedwarsPlayer(Player player) {
        this.playerUUID = player.getUniqueId();
        this.status = PlayerStatus.PREGAME;
        this.deaths = 0;
        this.kills = 0;
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
}
