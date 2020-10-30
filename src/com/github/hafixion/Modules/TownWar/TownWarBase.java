package com.github.hafixion.Modules.TownWar;

import com.github.hafixion.Utils.FileUtils;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TownWarBase {
    public static Path database = Paths.get("plugins/Feudalism/data/townwar");

    public static boolean isTownAtWar(Town town) {
        // prepare config
        boolean result = false;
        YamlConfiguration wardata = new YamlConfiguration();
        try {
            wardata.load(FileUtils.warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        List<String> wars = new ArrayList<>(Arrays.asList(wardata.getString("wars").split("_")));
         if (wars.contains(town.getUuid().toString())) {
             result = true;
         }
         return result;
    }

    public static boolean isNationAtWar(Nation nation) {
        // prepare config
        boolean result = false;
        YamlConfiguration wardata = new YamlConfiguration();
        try {
            wardata.load(FileUtils.warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        List<String> wars = new ArrayList<>(Arrays.asList(wardata.getString("nations").split("_")));
        if (wars.contains(nation.getUuid().toString())) {
            result = true;
        }
        return result;
    }

    public static List<File> getWarFiles(UUID uuid) {
        List<File> files = null;
        YamlConfiguration config = new YamlConfiguration();

        for (File data : database.toFile().listFiles()) {
            try {
                config.load(data);
                if (config.getString("attacker").equals(uuid.toString()) || config.getString("defender").equals(uuid.toString())) {
                    files.add(data);
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        return files;
    }

    public static List<UUID> getDefenders(UUID uuid) {
        List<UUID> towns = null;
        YamlConfiguration config = new YamlConfiguration();

        for (File data : database.toFile().listFiles()) {
            try {
                config.load(data);
                if (config.getString("attacker").equals(uuid.toString())) {
                    towns.add(UUID.fromString(config.getString("defender")));
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        return towns;
    }

    public static List<UUID> getAttackers(UUID uuid) {
        List<UUID> nations = null;
        YamlConfiguration config = new YamlConfiguration();

        for (File data : database.toFile().listFiles()) {
            try {
                config.load(data);
                if (config.getString("defender") == uuid.toString()) {
                    nations.add(UUID.fromString(config.getString("attacker")));
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        return nations;
    }

    public static File getWarFile(UUID nation, UUID town) {
        File warfile = null;
        YamlConfiguration config = new YamlConfiguration();

        for (File file : getWarFiles(town)) {
            try {
                config.load(warfile);
                if (config.getString("attacker") == nation.toString()) {
                    warfile = file;
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        return warfile;
    }

}
