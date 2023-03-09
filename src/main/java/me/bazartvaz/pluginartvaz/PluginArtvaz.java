package me.bazartvaz.pluginartvaz;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginArtvaz extends JavaPlugin{

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Pora na zavod");
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getCommand("hunter").setExecutor(new SpeedRunnerVSHunters());
        getCommand("speedrunner").setExecutor(new SpeedRunnerVSHunters());
        getCommand("startgame").setExecutor(new SpeedRunnerVSHunters());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Otval");
    }

}
