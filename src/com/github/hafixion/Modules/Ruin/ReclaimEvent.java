package com.github.hafixion.Modules.Ruin;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReclaimEvent extends Event implements Cancellable {
    public Resident resident;
    public Town town;
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public ReclaimEvent(Town town, Resident resident) {

    }
}
