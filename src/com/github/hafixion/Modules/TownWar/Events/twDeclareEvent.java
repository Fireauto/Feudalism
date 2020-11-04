package com.github.hafixion.Modules.TownWar.Events;

import com.github.hafixion.Modules.TownWar.TownWar;
import com.github.hafixion.Modules.TownWar.TownWarBase;
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
import java.util.*;

public class twDeclareEvent extends Event implements Cancellable {
    public HandlerList handlerList;
    public boolean cancelled;
    public Nation nation;
    public Town town;
    public File file;
    public YamlConfiguration wardata = new YamlConfiguration();
    public TownWar townWar;

    // actual event shit
    public twDeclareEvent(Nation nation, Town town) {

        // town war obj
        UUID uuid = UUID.randomUUID();
        File file = new File(TownWarBase.database.toString(), uuid + ".yml");
        townWar = new TownWar(nation, town, file, uuid);

        List<Town> townList = new LinkedList<>(nation.getTowns());
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
             WarlistUtils.AddTowntoWarList(town1.getUuid());
        }

        WarlistUtils.AddNationtoWarList(nation.getUuid());
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

    public TownWar getTownWar() {
        return townWar;
    }
}
