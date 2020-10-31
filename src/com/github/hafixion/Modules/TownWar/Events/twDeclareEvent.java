package com.github.hafixion.Modules.TownWar.Events;

import com.github.hafixion.Modules.Ruin.RuinBase;
import com.github.hafixion.Utils.WarlistUtils;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class twDeclareEvent extends Event implements Cancellable {
    public HandlerList handlerList;
    public boolean cancelled;
    public Nation nation;
    public Town town;
    public File file;
    public UUID uuid = UUID.randomUUID();
    public YamlConfiguration wardata = new YamlConfiguration();

    // actual event shit
    public twDeclareEvent(Nation nation, Town town) {
        file = new File(RuinBase.database.toString(), uuid + ".yml");
        YamlConfiguration filedata = new YamlConfiguration();
        filedata.set("attacker", nation.getUuid());
        filedata.set("defender", town.getUuid());
        filedata.set("warscore", 0);
        filedata.set("time", System.currentTimeMillis());
        try {
            filedata.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Town> townList = nation.getTowns();
        townList.add(town);

        for (Town town1 : townList) {

            town1.setAdminEnabledPVP(true);
            List<Resident> residents = town1.getResidents();
            for (Resident resident : residents) {
                for (TownBlock townBlock : resident.getTownBlocks()) {
                    try {
                        // complex way of saying, if the plot's town is in the attacking nation
                        if (townBlock.getTown() == town1 && town1.getNation() == nation && resident.getTown() == town) {
                            townBlock.setResident(null);
                        }
                        // else if the plot is part of the town and he's part of the nation
                        else if (townBlock.getTown() == town && resident.getTown().getNation() == nation) {
                            townBlock.setResident(null);
                        }
                    } catch (NotRegisteredException e) {
                        e.printStackTrace();
                    }
                }
            }

            // adding town to warlist
            try {
                wardata.load(WarlistUtils.warlist);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            List<String> towns = new ArrayList<>(Arrays.asList(wardata.getString("towns").split("-")));
            towns.add(town1.getUuid().toString());
            wardata.set("towns", String.join("-", towns));
        }

        // adding nation to warlist
        try {
            wardata.load(WarlistUtils.warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        List<String> nations = new ArrayList<>(Arrays.asList(wardata.getString("nations").split("-")));
        nations.add(nation.getUuid().toString());
        wardata.set("nations", String.join("-", nations));
    }

    // mandatory stuff ignore
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public File getFile() {
        return file;
    }

    public HandlerList getHandlerList() {
        return handlerList;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }
}
