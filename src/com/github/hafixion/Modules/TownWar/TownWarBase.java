package com.github.hafixion.Modules.TownWar;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TownWarBase {
    public static Path database = Paths.get("plugins/Feudalism/data/townwar");

    public static List<File> getWarFilesfromTowny(UUID uuid) {
        List<File> files = new LinkedList<>();
        YamlConfiguration config = new YamlConfiguration();

        if (database.toFile().listFiles() != null) {
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
        }

        return files;
    }

    public static List<UUID> getDefenders(UUID uuid) {
        List<UUID> towns = new LinkedList<>();
        YamlConfiguration config = new YamlConfiguration();

        if (database.toFile().listFiles() != null) {
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
        }

        return towns;
    }

    public static List<UUID> getAttackers(UUID uuid) {
        List<UUID> nations = new LinkedList<>();
        YamlConfiguration config = new YamlConfiguration();

        if (database.toFile().listFiles() != null) {
            for (File data : database.toFile().listFiles()) {
                try {
                    config.load(data);
                    if (config.getString("defender").equals(uuid.toString())) {
                        nations.add(UUID.fromString(config.getString("attacker")));
                    }
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }

        return nations;
    }

    public static File getWarFilefromTowny(UUID nation, UUID town) {
        File warfile = null;
        YamlConfiguration config = new YamlConfiguration();

        if (getWarFilesfromTowny(town) != null) {
            for (File file : getWarFilesfromTowny(town)) {
                try {
                    config.load(file);
                    if (config.getString("attacker").equals(nation.toString())) {
                        warfile = file;
                    }
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }

        return warfile;
    }

    public static File getWarFile(UUID uuid) {
        File warfile = null;
        YamlConfiguration config = new YamlConfiguration();

        if (database.toFile().listFiles() != null) {
            for (File file : TownWarBase.database.toFile().listFiles()) {
                try {
                    config.load(file);
                    if (String.valueOf(config.get("uuid")).equals(uuid.toString())) {
                        warfile = file;
                    }
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }

            }
        }

        return warfile;
    }

    // TownWar obj additions

    public static TownWar getTownWarfromTowny(Nation nation, Town town) {
        TownWar war = new TownWar();
        File file = getWarFilefromTowny(nation.getUuid(), town.getUuid());

        if (file != null) {
            war.setFile(file);
        }

        return war;
    }

    public static TownWar getTownWarfromUUID(UUID uuid) {
        TownWar war = new TownWar();
        File file = getWarFile(uuid);

        war.setFile(file);

        return war;
    }

}
