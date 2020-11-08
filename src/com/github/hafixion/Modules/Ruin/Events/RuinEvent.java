package com.github.hafixion.Modules.Ruin.Events;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Modules.Ruin.RuinBase;
import com.github.hafixion.Utils.ChatInfo;
import com.palmergames.bukkit.towny.command.TownyAdminCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;
import java.io.IOException;

public class RuinEvent extends Event implements Cancellable {
    private static Town town;
    private static File file;
    public boolean cancelled;
    private static HandlerList handlers = new HandlerList();
    private static final TownyAdminCommand adminCommand = new TownyAdminCommand(null);

    // bukkit requirements for event
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    // when the event is called create the file and save the values.
    public RuinEvent(Town town) {
        try {
            // file creation process
            file = new File(RuinBase.database.toString(), town.getUuid().toString() + ".yml");
            YamlConfiguration filedata = new YamlConfiguration();
            filedata.set("uuid", file.getName());
            filedata.set("time", System.currentTimeMillis());
            filedata.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // nation stuff
        if (town.isCapital()) {
            try {
                adminCommand.parseAdminNationCommand(new String[] {town.getNation().getName(), "delete"});
            } catch (TownyException e) {
                e.printStackTrace();
            }
        }
        // set mayor to be npc
        try {
            adminCommand.adminSet(new String[]{"mayor", town.getName(), "npc"});
        } catch (TownyException e) {
            e.printStackTrace();
        }
        // toggle and board stuff
        town.setBoard(town.getName() + " has fallen into ruin!");
        town.getMayor().setTitle("Ruined Mayor ");
        town.setPublic(false);
        town.setOpen(false);
        town.setPVP(true);
        Resident resident = town.getMayor();
        // enable permission
        if (FeudalismMain.plugin.getConfig().getBoolean("enable-permissions")) {
            try {
                for (String element : new String[]{"residentBuild",
                        "residentDestroy", "residentSwitch",
                        "residentItemUse", "outsiderBuild",
                        "outsiderDestroy", "outsiderSwitch",
                        "outsiderItemUse", "allyBuild", "allyDestroy",
                        "allySwitch", "allyItemUse", "nationBuild", "nationDestroy",
                        "nationSwitch", "nationItemUse",
                        "pvp", "fire", "explosion", "mobs"}) {
                    town.getPermissions().set(element, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            TownyAdminCommand adminCommand = new TownyAdminCommand(null);
            // make all plots have default permission, helps with raids in owned plots.
            adminCommand.parseAdminTownCommand(new String[]{town.getName(), "set", "perm", "reset"});
        } catch (Exception e) {
            System.out.println("Problem propagating perm changes to individual plots");
            e.printStackTrace();
        }
        Bukkit.broadcastMessage(ChatInfo.prefix("&c" + town.getName() + " has become a ruined town!"));
    }

    public static Town getTown() {
        return town;
    }

    public static File getFile() {
        return file;
    }

    public static void setTown(Town town) {
        RuinEvent.town = town;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
