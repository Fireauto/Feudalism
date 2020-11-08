package com.github.hafixion.Modules.Ruin;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Modules.Ruin.Events.RuinEvent;
import com.github.hafixion.Utils.ChatInfo;
import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
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
            } else {event.setCancelled(false);}
        } else {event.setCancelled(false);}
    }

    @SuppressWarnings("AccessStaticViaInstance")
    @EventHandler
    // when a town falls into ruin, ask the king to set a new mayor
    public static void onTownRuin(RuinEvent event) {
        try {
            if (event.getTown().hasNation()) {
                event.getTown().getNation().getKing().getPlayer().sendMessage(ChatInfo.prefix("&b" + event.getTown() + " has fallen into ruin, you should assign a mayor before it disappears."));
            }
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
    }
}
