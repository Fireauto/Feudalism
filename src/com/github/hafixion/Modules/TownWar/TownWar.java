package com.github.hafixion.Modules.TownWar;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TownWar {
    public int warscore;
    public Town town;
    public Nation nation;
    public long time;
    public UUID uuid;
    public File file;
    public int killscore;

    // this kinda just controls all townwars

    public TownWar() {
        this.warscore = 0;
        this.nation = null;
        this.town = null;
        this.time = System.currentTimeMillis();
        this.uuid = null;
        this.killscore = 0;
    }

    public TownWar(Nation nation, Town town, File file, UUID uuid) {
        this.nation = nation;
        this.town = town;
        this.uuid = uuid;
        this.time = System.currentTimeMillis();
        this.file = file;
        this.killscore = 0;

        // file editing
        YamlConfiguration filedata = new YamlConfiguration();

        filedata.set("uuid", uuid.toString());
        filedata.set("attacker", nation.getUuid());
        filedata.set("defender", town.getUuid());
        filedata.set("warscore", 0);
        filedata.set("time", time);
        filedata.set("killscore", 0);

        try {
            filedata.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTown(Town town) {
        this.town = town;
        File file = getFile();
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
            config.set("defender", town.getUuid());
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Town getTown() {
        return town;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
        File file = getFile();
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
            config.set("attacker", nation.getUuid());
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Nation getNation() {
        return nation;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        File file = getFile();
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
            config.set("time", time);
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public int getWarscore() {
        return warscore;
    }

    public void setWarscore(int warscore) {
        this.warscore = warscore;
        File file = getFile();
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
            config.set("warscore", warscore);
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        File file = getFile();
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
            config.set("uuid", uuid.toString());
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        YamlConfiguration config = new YamlConfiguration();
        this.file = file;

        try {
            config.load(file);
            this.nation = TownyUniverse.getInstance().getDataSource().getNation(config.getString("attacker"));
            this.town = TownyUniverse.getInstance().getDataSource().getTown(config.getString("defender"));
            this.uuid = UUID.fromString(config.getString("uuid"));
            this.time = config.getLong("time");
            this.warscore = config.getInt("warscore");
            this.killscore = config.getInt("killscore");
        } catch (IOException | InvalidConfigurationException | NotRegisteredException e) {
            e.printStackTrace();
        }
    }

    public int getKillscore() {
        return killscore;
    }

    public void setKillscore(int killscore) {
        this.killscore = killscore;
        File file = getFile();
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
            config.set("killscore", killscore);
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
