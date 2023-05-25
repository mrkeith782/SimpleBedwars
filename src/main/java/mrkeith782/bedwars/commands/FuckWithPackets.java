package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import net.minecraft.world.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FuckWithPackets implements CommandExecutor {
    private final Bedwars bedwars = Bedwars.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { //all of my commands atm are for players so i'm happy with this
            return false;
        }

        Player player = (((Player) sender).getPlayer());
        CraftPlayer cp = (CraftPlayer) player;

        if (cp == null) {
            return false;
        }

        Entity newEntity = new Villager(EntityType.VILLAGER, cp.getHandle().getCommandSenderWorld());
        ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(
                newEntity,
                0,
                new BlockPos(-10, 63, 10)
        );

        BukkitRunnable temp = new BukkitRunnable() {
            final Location entityLoc = new Location(player.getWorld(), -10, 63, 10);
            @Override
            public void run() {
                Location playerLoc = player.getLocation();

                Vector locDiff = playerLoc.toVector().subtract(entityLoc.toVector()).normalize();

                ClientboundMoveEntityPacket.Rot packet1 = new ClientboundMoveEntityPacket.Rot(
                    newEntity.getId(),
                    (byte) 0,
                    (byte) ((Math.asin(-locDiff.getY()) * (256.0F / 360.0F)) * 75),
                    false
                );

                ClientboundRotateHeadPacket packet2 = new ClientboundRotateHeadPacket(
                    newEntity,
                    (byte) (-(Math.atan2(locDiff.getX(), locDiff.getZ()) * (256.0F / 360.0F)) * 60)
                );

                cp.getHandle().connection.send(packet1);
                cp.getHandle().connection.send(packet2);
            }
        };

        temp.runTaskTimerAsynchronously(bedwars, 1L, 1L);

        cp.getHandle().connection.send(packet);
        return true;
    }
}
