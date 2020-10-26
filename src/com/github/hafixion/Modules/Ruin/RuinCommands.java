package com.github.hafixion.Modules.Ruin;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Modules.Ruin.Events.ReclaimEvent;
import com.github.hafixion.Utils.ChatInfo;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class RuinCommands {

    public static void exec(CommandSender sender, String[] args) {
        try {
            Resident resident = TownyAPI.getInstance().getDataSource().getResident(sender.getName());
            // check the args
            switch (args[0]) {
                // for regular town reclaiming
                case "reclaim":
                    if (FeudalismMain.plugin.getConfig().getBoolean("resident-reclaiming-enabled")) {
                        if (resident.hasTown() && RuinBase.isRuined(resident.getTown())) {
                            Confirmation.runOnAccept(() -> {
                                ReclaimEvent event;
                                try {
                                    event = new ReclaimEvent(resident.getTown(), resident);
                                    if (!event.isCancelled())
                                        FeudalismMain.plugin.getServer().getPluginManager().callEvent(event);
                                } catch (NotRegisteredException e) {
                                    e.printStackTrace();
                                }
                            }).setTitle("§bAre you sure you want to reclaim " + resident.getTown() + "?").sendTo(sender);
                        } else {
                            sender.sendMessage(ChatInfo.color("&cYou don't have a town, or your town isn't ruined."));
                        }
                    } else {
                        sender.sendMessage(ChatInfo.color("&cReclaiming is disabled."));
                    }
                    break;

                    // for nation reclaiming
                case "assign":
                    if (FeudalismMain.plugin.getConfig().getBoolean("nation-reclaiming-enabled")) {
                        if (args.length >= 3) {
                            if (sender.hasPermission(FeudalismMain.plugin.getConfig().getString("nation-reclaiming-permission"))) {
                                if (resident.hasTown() && resident.getTown().hasNation()) {
                                    if (Bukkit.getPlayer(args[1]).isOnline() || Bukkit.getPlayer(args[1]) != null) {
                                        if (TownyUniverse.getInstance().getDataSource().getTown(args[2]) != null) {
                                            Town town = TownyUniverse.getInstance().getDataSource().getTown(args[2]);
                                            if (resident.getTown().getNation() == town.getNation()) {
                                                if (RuinBase.isRuined(town)) {
                                                    Resident newmayor = TownyUniverse.getInstance().getDataSource().getResident(args[1]);
                                                    // lambda statement for /confirm
                                                    Confirmation.runOnAccept(() -> Confirmation.runOnAccept(() -> {
                                                        ReclaimEvent event;
                                                        event = new ReclaimEvent(town, resident);
                                                        if (!event.isCancelled())
                                                            FeudalismMain.plugin.getServer().getPluginManager().callEvent(event);
                                                    }).setTitle("§2" + sender.getName() + " wants you to reclaim " + town.getName() + " for the nation.").runOnCancel(() -> sender.sendMessage(ChatInfo.color("&b" + newmayor.getName() + " refused your suggestion"))).sendTo(newmayor.getPlayer())).setTitle("§bAre you sure you want " + args[1] + " to reclaim " + args[2]).sendTo(sender);
                                                    // under this is all the responses if an argument is wrong.
                                                } else {
                                                    sender.sendMessage(ChatInfo.color("&c" + args[2] + " is not ruined."));
                                                }
                                            } else {
                                                sender.sendMessage(ChatInfo.color("&c" + args[2] + " is not in your nation."));
                                            }
                                        } else {
                                            sender.sendMessage(ChatInfo.color("&c" + args[2] + " is not registered."));
                                        }
                                    } else {
                                        sender.sendMessage(ChatInfo.color("&c" + args[1] + " is not online."));
                                    }
                                } else {
                                    sender.sendMessage(ChatInfo.color("&cYou don't have a nation or a town."));
                                }
                            } else {
                                sender.sendMessage(ChatInfo.color("&cYou don't have enough permissions."));
                            }
                        } else {
                            sender.sendMessage(ChatInfo.color("&cNot enough arguments."));
                        }
                    } else {
                        sender.sendMessage(ChatInfo.color("&cNation Reclaiming is disabled."));
                    }
                    break;
                default:
                    sender.sendMessage(ChatInfo.color("&c is not registered."));
                    break;
            }
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
    }
}
