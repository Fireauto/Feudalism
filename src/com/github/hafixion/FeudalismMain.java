package com.github.hafixion;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class FeudalismMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // if towny is loaded then enable the plugin
        if (getServer().getPluginManager().getPlugin("Towny").isEnabled()) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[Feudalism]&a Plugin Loaded Successfully"));
            register();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[Feudalism]&c Towny not enabled, unloading plugin"));
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[Feudalism]&c Plugin Unloaded Successfully"));
    }

    // register functions
    private void register() {
        saveDefaultConfig();
    }
}
