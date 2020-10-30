package com.github.hafixion.Modules.TownWar.Events;

import com.github.hafixion.Modules.TownWar.TownWarBase;
import com.github.hafixion.Utils.ChatInfo;
import com.github.hafixion.Utils.WarlistUtils;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;

public class twNationVictoryEvent extends Event {
    public Town town;
    public Nation nation;
    public HandlerList handlerList;

    public twNationVictoryEvent(Nation nation, Town town) {
        nation.addTown(town);
        Bukkit.broadcastMessage(ChatInfo.color("&b" + nation.getName() + " has successfully subjugated " + town.getName()));

        File file = TownWarBase.getWarFile(nation.getUuid(), town.getUuid());
        file.delete();

        for (Town town1 : nation.getTowns()) {
            WarlistUtils.RemoveTownfromWarList(town1.getUuid());
        }
        WarlistUtils.RemoveTownfromWarList(town.getUuid());
        // to be continued once i add wargoals and rebellions and stuff
    }

    // Mandatory Bukkit stuff

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public HandlerList getHandlerList() {
        return handlerList;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public Nation getNation() {
        return nation;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Town getTown() {
        return town;
    }
}
