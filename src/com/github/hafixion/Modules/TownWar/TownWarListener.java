package com.github.hafixion.Modules.TownWar;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Modules.Ruin.Events.RuinEvent;
import com.github.hafixion.Modules.TownWar.Events.twNationVictoryEvent;
import com.github.hafixion.Utils.ChatInfo;
import com.github.hafixion.Utils.WarlistUtils;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.*;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class TownWarListener implements Listener {
    public boolean isMerging = false;
    public Town capital;
    public List<Town> townList;

    // Teleport stuff
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

    @EventHandler
    public void onNationTeleport(NationSpawnEvent event) throws NotRegisteredException {

        if (TownWarBase.isTownAtWar(TownyUniverse.getInstance().getDataSource().getTown(event.getPlayer().getName()))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatInfo.color("&cYou cannot teleport while at war."));
        }

        if (TownWarBase.isNationAtWar(event.getToNation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatInfo.color("&c" + event.getToNation().getName() + " is currently at war."));
        }

    }

    @EventHandler
    // when a dumbass invites a town at war
    public void onNationInvite(NationInviteTownEvent event) {

        if (TownWarBase.isTownAtWar(event.getInvite().getReceiver())) {
            event.getInvite().decline(true);
            event.getInvite().getDirectSender().sendMessage(ChatInfo.color("&c" + event.getInvite().getReceiver().getName() + " is at war, they cannot join a town at this time."));
        }

    }

    @EventHandler
    // when a dumbass joins a nation at war
    public void onNationJoin(NationAddTownEvent event) {

        if (TownWarBase.isNationAtWar(event.getNation())) {
            WarlistUtils.AddTowntoWarList(event.getTown().getUuid());
        }

    }

    @EventHandler
    // when a town leaves a nation at war (somehow)
    public void onNationLeave(NationRemoveTownEvent event) {

        // this is just to make sure it wasn't a merge
        if (!event.getTown().hasNation()) {
            if (TownWarBase.isNationAtWar(event.getNation())) {
                WarlistUtils.RemoveTownfromWarList(event.getTown().getUuid());
            }
        }

    }

    @EventHandler
    public void onNationPreDelete(PreDeleteNationEvent event) {
        capital = event.getNation().getCapital();
        townList = event.getNation().getTowns();
    }

    // not using event because it waits to check if it's a merge and thus loses the nation var
    @EventHandler
    public void onNationDelete(DeleteNationEvent event) throws NotRegisteredException {
        if (capital.hasNation()) {
            isMerging = true;
        } else {
            isMerging = false;
            for (UUID uuid : TownWarBase.getDefenders(event.getNationUUID())) {
                Town town = TownyUniverse.getInstance().getDataSource().getTown(uuid);
                TownyMessaging.sendGlobalMessage(ChatInfo.color("&b" + event.getNationName() + " has collapsed, " + town.getName() + " is now at peace."));
                WarlistUtils.RemoveTownfromWarList(uuid);
            }

            WarlistUtils.RemoveNationfromWarList(event.getNationUUID());
            for (Town town : townList) {
                WarlistUtils.RemoveTownfromWarList(town.getUuid());
            }
        }
    }

    @EventHandler
    public void onNationMerge(NationPreMergeEvent event) {
        // todo change war to merged nation
    }

    @EventHandler
    public void onTownRuin(RuinEvent event) throws NotRegisteredException, IOException, InvalidConfigurationException {
        int warscore = 0;
        Town town = event.getTown();
        YamlConfiguration config = new YamlConfiguration();
        Nation victor = null;

        /* the actual stuff, overcomplicated but basically check if bigger than warscore and if it is set it as the warscore
        and set the nation as the victor, keep going until all nations are done and you should have the victor
         */
        if (TownWarBase.isTownAtWar(town)) {
            for (UUID uuid : TownWarBase.getAttackers(town.getUuid())) {
                Nation nation = TownyUniverse.getInstance().getDataSource().getNation(uuid);

                for (File file : TownWarBase.getWarFiles(town.getUuid())) {
                    config.load(file);
                    if (warscore < config.getInt("warscore")) {
                        warscore = config.getInt("warscore");
                        victor = nation;
                    }
                }
            }
            if (victor != null) {
                twNationVictoryEvent victoryEvent = new twNationVictoryEvent(victor, town);
                FeudalismMain.plugin.getServer().getPluginManager().callEvent(victoryEvent);
            }
        }
    }

    @EventHandler
    public void onPreNewNation(PreNewNationEvent event) {
        if (TownWarBase.isTownAtWar(event.getTown())) {
            event.setCancelled(true);
            event.getTown().getMayor().getPlayer().sendMessage(ChatInfo.color("&cCannot create a nation while at war."));
        }
    }
}
