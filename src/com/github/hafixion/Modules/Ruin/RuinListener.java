package com.github.hafixion.Modules.Ruin;

import com.github.hafixion.FeudalismMain;
import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RuinListener implements Listener {

    @EventHandler
    public static void onTownFall(PreDeleteTownEvent event) {
        if (FeudalismMain.plugin.getConfig().getBoolean("ruin-enabled")) {
            if (!event.getTown().getMayor().isNPC()) {
                event.setCancelled(true);
                RuinEvent ruinEvent = new RuinEvent(event.getTown());
                if (!ruinEvent.cancelled) FeudalismMain.plugin.getServer().getPluginManager().callEvent(ruinEvent);
            }
        }
    }
}
