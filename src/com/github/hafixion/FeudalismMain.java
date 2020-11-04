package com.github.hafixion;

import com.github.hafixion.Commands.FeudalismCommand;
import com.github.hafixion.Modules.Ruin.RuinListener;
import com.github.hafixion.Modules.TownWar.TownWarListener;
import com.github.hafixion.Utils.ChatInfo;
import com.github.hafixion.Utils.WarlistUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
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
        YamlConfiguration config = new YamlConfiguration();
        
        // files
        saveDefaultConfig();
        if (!WarlistUtils.warlist.exists()) {
            WarlistUtils.warlist.mkdir();
        }
        // listeners
        getServer().getPluginManager().registerEvents(new RuinListener(), plugin);
        getServer().getPluginManager().registerEvents(new TownWarListener(), plugin);
        // commands
        PluginCommand fd = getCommand("feudalism");
        fd.setExecutor(new FeudalismCommand());
        fd.setTabCompleter(new FeudalismCommand.FeudalismTabCompleter());

    }
}
