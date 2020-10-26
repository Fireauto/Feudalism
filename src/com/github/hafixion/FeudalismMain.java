package com.github.hafixion;

import com.github.hafixion.Commands.FeudalismCommand;
import com.github.hafixion.Modules.Ruin.RuinListener;
import com.github.hafixion.Utils.ChatInfo;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FeudalismMain extends JavaPlugin {
    public static Plugin plugin;
    public static void setPlugin(Plugin plugin) {FeudalismMain.plugin = plugin;}

    @Override
    public void onEnable() {
        // set the plugin var
        setPlugin(this);
        // if towny is loaded then enable the plugin
        if (getServer().getPluginManager().getPlugin("Towny").isEnabled()) {
            getServer().getConsoleSender().sendMessage(ChatInfo.color("&a Plugin Loaded Successfully"));
            register();
        } else {
            getServer().getConsoleSender().sendMessage(ChatInfo.color("&c Towny not enabled, unloading plugin"));
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatInfo.color("&c Plugin Unloaded Successfully"));
    }

    // register functions
    private void register() {
        saveDefaultConfig();
        // listeners
        getServer().getPluginManager().registerEvents(new RuinListener(), plugin);
        // commands
        PluginCommand fd = getCommand("feudalism");
        fd.setExecutor(new FeudalismCommand());
        fd.setTabCompleter(new FeudalismCommand.FeudalismTabCompleter());

    }
}
