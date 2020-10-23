package com.github.hafixion.Modules.Ruin;

import com.github.hafixion.Utils.FileUtils;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RuinEvent extends Event implements Cancellable {
    private static Town town;
    private static File file;
    private static YamlConfiguration filedata;
    public boolean cancelled;
    protected Path database = Paths.get("plugins/Feudalism/data/ruinedtowns");

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
        file = FileUtils.createYAMLFile(town.getUuid().toString(), database);
        filedata.set("uuid", town.getUuid().toString());
        filedata.set("time", System.currentTimeMillis());
        try {
            filedata.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
