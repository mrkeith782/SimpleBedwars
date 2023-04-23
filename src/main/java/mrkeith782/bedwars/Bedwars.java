package mrkeith782.bedwars;

import mrkeith782.bedwars.Commands.ArmorStandCommand;
import mrkeith782.bedwars.Commands.HelloWorldCommand;
import mrkeith782.bedwars.Util.ArmorStandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {
    private static Bedwars instance;
    public ArmorStandManager asm;



    @Override
    public void onEnable() {
        instance = this;

        asm = new ArmorStandManager();

        getCommand("hello").setExecutor(new HelloWorldCommand());
        getCommand("placearmorstand").setExecutor(new ArmorStandCommand());
        getCommand("printarmorstands").setExecutor(new ArmorStandCommand());
        getCommand("editarmorstand").setExecutor(new ArmorStandCommand());
    }

    @Override
    public void onDisable() {
        asm.removeAllArmorStands();
    }

    public static Bedwars getInstance() {
        return instance;
    }

    public ArmorStandManager getAsm() {
        return asm;
    }
}
