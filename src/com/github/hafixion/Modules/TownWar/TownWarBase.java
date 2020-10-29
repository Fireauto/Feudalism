package com.github.hafixion.Modules.TownWar;

import com.github.hafixion.Utils.FileUtils;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<String> wars = new ArrayList<>(Arrays.asList(wardata.getString("wars").split("-")));
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

        List<String> wars = new ArrayList<>(Arrays.asList(wardata.getString("nations").split("-")));
        if (wars.contains(nation.getUuid().toString())) {
            result = true;
        }
        return result;
    }

    

}
