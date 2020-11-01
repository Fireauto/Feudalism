package com.github.hafixion.Modules.TownWar.Events;

import com.github.hafixion.Modules.TownWar.TownWar;
import com.github.hafixion.Modules.TownWar.TownWarBase;
import com.github.hafixion.Utils.ChatInfo;
import com.github.hafixion.Utils.WarlistUtils;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;

public class twTownVictoryEvent extends Event {

    public TownWar war;
    public HandlerList handlerList;

    public twTownVictoryEvent(TownWar townwar) {
        this.war = townwar;
        Nation nation = townwar.getNation();
        Town town = townwar.getTown();
        Bukkit.broadcastMessage(ChatInfo.color("&b" + townwar.getTown().getName() + " has successfully resisted " + townwar.getNation().getName()));

        File file = townwar.getFile();
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

    public TownWar getWar() {
        return war;
    }

    public void setWar(TownWar war) {
        this.war = war;
    }
}
