package com.github.hafixion.Modules.Ruin.Events;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Modules.Ruin.RuinBase;
import com.github.hafixion.Utils.ChatInfo;
import com.github.hafixion.Utils.FileUtils;
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
    private static YamlConfiguration filedata;
    public boolean cancelled;
    private static final TownyAdminCommand adminCommand = new TownyAdminCommand(null);

    // bukkit requirements for event
    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    // when the event is called create the file and save the values.
    public RuinEvent(Town town) {
        // file creation process
        file = FileUtils.createYAMLFile(town.getUuid().toString(), RuinBase.database);
        filedata.set("uuid", town.getUuid().toString());
        filedata.set("time", System.currentTimeMillis());
        try {
            filedata.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // actual town editing
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
        Bukkit.broadcastMessage(ChatInfo.color("&c" + town.getName() + " has become a ruined town!"));
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

    public static YamlConfiguration getFiledata() {
        return filedata;
    }
}
