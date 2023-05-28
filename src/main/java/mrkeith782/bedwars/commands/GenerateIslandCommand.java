package mrkeith782.bedwars.commands;

import mrkeith782.bedwars.Bedwars;
import mrkeith782.bedwars.managers.ArmorStandManager;
import mrkeith782.bedwars.managers.NPCManager;
import mrkeith782.bedwars.npcs.ShopNPC;
import mrkeith782.bedwars.npcs.UpgradeNPC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GenerateIslandCommand implements CommandExecutor {
    private final Bedwars bedwars = Bedwars.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        NPCManager npcm = bedwars.getNpcm();
        ArmorStandManager asm = bedwars.getAsm();
        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            npcm.removeAllNPCs();
            asm.removeAllTextDisplays();
            asm.removeAllArmorStands();
            return true;
        }

        npcm.spawnAndStoreNPC(new ShopNPC(180), new Location(player.getWorld(), -12.5, 69.0, 4.5));
        asm.spawnNewTextDisplay(new Location(player.getWorld(), -12.5, 71.6, 4.9), "%%aqua%%SHOP", "SHOP_NPC_2", 180);
        asm.spawnNewTextDisplay(new Location(player.getWorld(), -12.5, 71.4, 4.9), "%%yellow%%%%bold%%RIGHT CLICK", "SHOP_NPC_1", 180);

        npcm.spawnAndStoreNPC(new UpgradeNPC(0), new Location(player.getWorld(), -12.5, 69.0, -1.5));
        asm.spawnNewTextDisplay(new Location(player.getWorld(), -12.5, 71.7, -1.9), "%%aqua%%SOLO", "UPGRADE_NPC_3", 0);
        asm.spawnNewTextDisplay(new Location(player.getWorld(), -12.5, 71.5, -1.9), "%%aqua%%UPGRADES", "UPGRADE_NPC_2", 0);
        asm.spawnNewTextDisplay(new Location(player.getWorld(), -12.5, 71.3, -1.9), "%%yellow%%%%bold%%RIGHT CLICK", "UPGRADE_NPC_1", 0);
        return true;
    }
}
