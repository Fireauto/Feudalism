package com.github.hafixion.Modules.Ruin.Events;

import com.github.hafixion.Modules.Ruin.RuinBase;
import com.github.hafixion.Utils.ChatInfo;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;

@SuppressWarnings("ALL")
public class ReclaimEvent extends Event implements Cancellable {
    public Resident resident;
    public Town town;
    private static HandlerList handlers = new HandlerList();
    private static boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    // the actual event
    public ReclaimEvent(Town town, Resident resident) {
        try {
            if (RuinBase.isRuined(town)) {
                // if they aren't part of that town
                if (resident.getTown() != town) {
                    resident.removeTown();
                    resident.setTown(town);
                }
                File file = RuinBase.getFile(town);
                file.delete();
                town.setMayor(resident);
                // set perms off
                for (String element : new String[]{"outsiderBuild",
                        "outsiderDestroy", "outsiderSwitch",
                        "outsiderItemUse", "allyBuild", "allyDestroy",
                        "allySwitch", "allyItemUse", "nationBuild", "nationDestroy",
                        "nationSwitch", "nationItemUse",
                        "pvp", "fire", "explosion", "mobs"}) {
                    town.getPermissions().set(element, false);
                }
                // clear npcs, reset the town, broadcast it
                RuinBase.ClearNPCs();
                town.setBoard(resident.getTown().getName() + " has returned under the leadership of " + resident.getName());
                town.setPVP(false);
                Bukkit.broadcastMessage(ChatInfo.prefix("&b" + town.getName() + " has returned under the leadership of " + resident.getName()));
            }
        } catch (TownyException e) {
            e.printStackTrace();
        }
    }

    public Resident getResident() {
        return resident;
    }

    public Town getTown() {
        return town;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
