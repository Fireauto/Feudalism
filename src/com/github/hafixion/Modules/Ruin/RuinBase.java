package com.github.hafixion.Modules.Ruin;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Utils.ChatInfo;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.command.TownyAdminCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RuinBase {
    public static Path database = Paths.get("plugins/Feudalism/data/ruinedtowns");
    private static final TownyAdminCommand adminCommand = new TownyAdminCommand(null);

    // getting the file of the ruined town
    public static File getFile(Town town) {
        File result = null;
        String filename = town.getUuid().toString() + ".yml";
        if (database.toFile().listFiles() != null) {
            for (File file : Arrays.asList(database.toFile().listFiles())) {
                if (file.getName().equals(filename)) result = file;
            }
        }
        return result;
    }

    // getting if the town is ruined function
    public static boolean isRuined(Town town) {
        boolean result = false;
        if (getFile(town) != null) result = true;
        return result;
    }

    // purges towns older than whatever value is specificed in config
    public static void ClearExpiredRuinedTowns() {
        YamlConfiguration config = new YamlConfiguration();
        if (database.toFile().listFiles() != null) {
            for (File file : Arrays.asList(database.toFile().listFiles())) {
                try {
                    config.load(file);
                    if (config.getLong("time") - System.currentTimeMillis() >= FeudalismMain.plugin.getConfig().getLong("time-till-expiration")) {
                        Town town = TownyUniverse.getInstance().getDataSource().getTown(config.getString("uuid"));
                        adminCommand.parseAdminTownCommand(new String[]{town.getName(), "delete"});
                        Bukkit.broadcastMessage(ChatInfo.color("&b" + town.getName() + " has finally fallen into history."));
                        //noinspection ResultOfMethodCallIgnored
                        file.delete();
                    }
                } catch (IOException | InvalidConfigurationException | TownyException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    // clear non-important npcs, so they don't appear on /res for example
    public static void ClearNPCs() {
        List<Resident> residentList = TownyUniverse.getInstance().getDataSource().getResidents();
        for (Resident resident : residentList) {
            if (resident.isNPC()) {
                if (!resident.isMayor()) {
                    TownyAPI.getInstance().getDataSource().removeResident(resident);
                }
            }
        }
    }
}
