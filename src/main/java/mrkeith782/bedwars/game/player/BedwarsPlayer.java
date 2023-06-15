package mrkeith782.bedwars.game.player;

import lombok.Getter;
import lombok.Setter;
import mrkeith782.bedwars.game.team.BedwarsTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BedwarsPlayer {
    UUID playerUUID;
    @Getter
    @Setter
    PlayerStatus status;
    @Getter
    @Setter
    BedwarsTeam team;
    @Getter
    int kills;
    @Getter
    int bedsBroken;
    @Getter
    int finalKills;

    @Setter
    boolean needsUpdate;

    public BedwarsPlayer(Player player) {
        this.playerUUID = player.getUniqueId();
        this.status = PlayerStatus.PREGAME;
        this.kills = 0;
        this.needsUpdate = false;
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

    public boolean getNeedsUpdate() {
        return this.needsUpdate;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerUUID);
    }
}
