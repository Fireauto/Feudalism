package com.github.hafixion.Utils;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarlistUtils {
    public static File warlist = new File("plugins/Feudalism/data/ruinedtowns", "warlist.yml");

    public static void AddTowntoWarList(UUID town) {
        // prepare config
        YamlConfiguration wardata = new YamlConfiguration();
        if (warlist.exists()) {
            try {
                wardata.load(warlist);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (wardata.getString("towns") != null) {
                List<String> towns = new LinkedList<>(Arrays.asList(Objects.requireNonNull(wardata.getString("towns")).split("_")));
                if (!towns.contains(town.toString())) {
                    towns.add(town.toString());
                    wardata.set("towns", String.join("_", towns));
                }
            } else {
                wardata.set("towns", town.toString());
            }
        } else {
             wardata.set("towns", town.toString());
        }

        try {
            wardata.save(warlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void RemoveTownfromWarList(UUID town) {
        // prepare config
        YamlConfiguration wardata = new YamlConfiguration();
        if (warlist.exists()) {
            try {
                wardata.load(warlist);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (wardata.getString("towns") != null) {
                List<String> towns = new LinkedList<>(Arrays.asList(wardata.getString("towns").split("_")));
                if (towns.contains(town.toString())) {
                    towns.remove(town.toString());
                    wardata.set("towns", String.join("_", towns));
                }
            }
        }

        try {
            wardata.save(warlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void AddNationtoWarList(UUID nation) {
        // prepare config
        YamlConfiguration wardata = new YamlConfiguration();
        if (warlist.exists()) {
            try {
                wardata.load(warlist);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (wardata.getString("nations") != null) {
                List<String> nations = new LinkedList<>(Arrays.asList(wardata.getString("nations").split("_")));
                if (!nations.contains(nation.toString())) {
                    nations.remove(nation.toString());
                    wardata.set("nations", String.join("_", nations));
                }
            } {wardata.set("nations", nation.toString());}
        } else {wardata.set("nations", nation.toString());}

        try {
            wardata.save(warlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void RemoveNationfromWarList(UUID nation) {
        // prepare config
        YamlConfiguration wardata = new YamlConfiguration();
        if (warlist.exists()) {
            try {
                wardata.load(warlist);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (wardata.getString("nations") != null) {
                List<String> nations = new LinkedList<>(Arrays.asList(wardata.getString("nations").split("_")));
                if (nations.contains(nation.toString())) {
                    nations.remove(nation.toString());
                    wardata.set("nations", String.join("_", nations));
                }
            }
        }

        try {
            wardata.save(warlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isTownAtWar(Town town) {
        // prepare config
        boolean result = false;
        YamlConfiguration wardata = new YamlConfiguration();
        if (warlist.exists()) {
            try {
                wardata.load(warlist);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (wardata.getString("towns") != null) {
                List<String> wars = new LinkedList<>(Arrays.asList(wardata.getString("towns").split("_")));
                if (wars.contains(town.getUuid().toString())) {
                    result = true;
                }
            }
        }
         return result;
    }

    public static boolean isNationAtWar(Nation nation) {
        // prepare config
        boolean result = false;
        YamlConfiguration wardata = new YamlConfiguration();
        if (warlist.exists()) {
            try {
                wardata.load(warlist);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            if (wardata.getString("nations") != null) {
                List<String> wars = new LinkedList<>(Arrays.asList(wardata.getString("nations").split("_")));
                if (wars.contains(nation.getUuid().toString())) {
                    result = true;
                }
            }
        }
        return result;
    }
}
