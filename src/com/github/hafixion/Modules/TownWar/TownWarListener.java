package com.github.hafixion.Modules.TownWar;

import com.github.hafixion.Utils.ChatInfo;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.TownSpawnEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownWarListener implements Listener {

    @EventHandler
    public void onTownTeleport(TownSpawnEvent event) throws NotRegisteredException {

        if (TownWarBase.isTownAtWar(TownyUniverse.getInstance().getDataSource().getTown(event.getPlayer().getName()))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatInfo.color("&cYou cannot teleport while at war."));
        }

        if (TownWarBase.isTownAtWar(event.getToTown())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatInfo.color("&c" + event.getToTown().getName() + " is currently at war."));
        }

    }
}
