package com.github.hafixion.Utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WarlistUtils {
    public static File warlist = new File("plugins/Feudalism/data/ruinedtowns", "warlist.yml");

    public static void AddTowntoWarList(UUID town) {
        // prepare config
        YamlConfiguration wardata = new YamlConfiguration();
        try {
            wardata.load(warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        List<String> towns = new ArrayList<>(Arrays.asList(wardata.getString("towns").split("_")));
        if (!towns.contains(town.toString())) {
            towns.add(town.toString());
            wardata.set("towns", String.join("_", towns));
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
        try {
            wardata.load(warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        List<String> towns = new ArrayList<>(Arrays.asList(wardata.getString("towns").split("_")));
        if (towns.contains(town.toString())) {
            towns.remove(town.toString());
            wardata.set("towns", String.join("_", towns));
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
        try {
            wardata.load(warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        List<String> nations = new ArrayList<>(Arrays.asList(wardata.getString("nations").split("_")));
        if (!nations.contains(nation.toString())) {
            nations.remove(nation.toString());
            wardata.set("towns", String.join("_", nations));
        }

        try {
            wardata.save(warlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void RemoveNationfromWarList(UUID nation) {
        // prepare config
        YamlConfiguration wardata = new YamlConfiguration();
        try {
            wardata.load(warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        List<String> nations = new ArrayList<>(Arrays.asList(wardata.getString("nations").split("_")));
        if (nations.contains(nation.toString())) {
            nations.remove(nation.toString());
            wardata.set("towns", String.join("_", nations));
        }

        try {
            wardata.save(warlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
