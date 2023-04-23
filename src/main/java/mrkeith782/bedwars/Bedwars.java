package mrkeith782.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("hello").setExecutor(new HelloWorldCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
