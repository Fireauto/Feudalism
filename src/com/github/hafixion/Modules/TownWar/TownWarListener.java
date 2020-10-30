package com.github.hafixion.Modules.TownWar;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Modules.Ruin.Events.RuinEvent;
import com.github.hafixion.Modules.TownWar.Events.twNationVictoryEvent;
import com.github.hafixion.Modules.TownWar.Events.twTownVictoryEvent;
import com.github.hafixion.Utils.ChatInfo;
import com.github.hafixion.Utils.FileUtils;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.*;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TownWarListener implements Listener {
    public Nation mergingnation;
    public Town mergingcapital;

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
            TownWarBase.AddTowntoWarList(event.getTown().getUuid());
        }

    }

    @EventHandler
    // when a town leaves a nation at war (somehow)
    public void onNationLeave(NationRemoveTownEvent event) {

        // this is just to make sure it wasn't a merge
        if (!event.getTown().hasNation()) {
            if (TownWarBase.isNationAtWar(event.getNation())) {
                TownWarBase.RemoveTownfromWarList(event.getTown().getUuid());
            }
        }

    }

    // Merging stuff and for when a nation is deleted, fuck towny not adding a merge event

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @EventHandler
    public void onNationPreDelete(PreDeleteNationEvent event) throws NotRegisteredException {
        mergingnation = event.getNation();
        mergingcapital = event.getNation().getCapital();

        // todo replace with twTownVictoryEvent
        if (TownWarBase.isNationAtWar(event.getNation())) {
            TownWarBase.RemoveNationfromWarList(event.getNation().getUuid());

            // removing towns from being marked as at war
            for (Town town : event.getNation().getTowns()) {
                TownWarBase.RemoveTownfromWarList(town.getUuid());
            }

            // deleting all war files related to it
            for (File file : TownWarBase.getWarFiles(event.getNation().getUuid())) {
                file.delete();
            }

            // broadcast a message announcing peace
            for (UUID uuid : TownWarBase.getDefenders(event.getNation().getUuid())) {
                Town town = TownyUniverse.getInstance().getDataSource().getTown(uuid);
                TownWarBase.RemoveTownfromWarList(uuid);
                TownyMessaging.sendGlobalMessage(ChatInfo.color("&b" + event.getNationName() + " has collapsed, " + town.getName() + " is now at peace with them."));
            }
        }
    }

    @EventHandler
    public void onNationDelete(DeleteNationEvent event) throws NotRegisteredException {
        // prepare config
        YamlConfiguration wardata = new YamlConfiguration();
        try {
            wardata.load(FileUtils.warlist);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        List<String> wars = new ArrayList<>(Arrays.asList(wardata.getString("nations").split("_")));
        if (wars.contains(event.getNationUUID().toString())) {
            if (mergingcapital.hasNation()) {
                TownWarBase.RemoveNationfromWarList(event.getNationUUID());
                TownWarBase.AddNationtoWarList(mergingcapital.getNation().getUuid());
            }
        }
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
                for (Town town1 : victor.getTowns()) {
                    TownWarBase.RemoveTownfromWarList(town1.getUuid());
                }
                TownWarBase.RemoveTownfromWarList(town.getUuid());
            }
        }
    }

    @EventHandler
    public void NewNationCommand(PlayerCommandPreprocessEvent event) throws NotRegisteredException {
        if (event.getMessage().equals("/n new") || event.getMessage().equals("/nation new")) {
            Resident resident = TownyUniverse.getInstance().getDataSource().getResident(event.getPlayer().getName());
            if (resident.hasTown() && resident.isMayor() && TownWarBase.isTownAtWar(resident.getTown())) {
                event.setCancelled(true);
            }
        }
    }

}
