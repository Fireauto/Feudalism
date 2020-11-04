package com.github.hafixion.Modules.TownWar;

import com.github.hafixion.FeudalismMain;
import com.github.hafixion.Modules.TownWar.Events.twDeclareEvent;
import com.github.hafixion.Utils.ChatInfo;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.command.CommandSender;

public class TownWarCommands {

    public static void exec(CommandSender sender, String[] args) {
        try {
            Resident resident = TownyUniverse.getInstance().getDataSource().getResident(sender.getName());

            switch (args[0]) {

                case "declare":
                    if (args.length == 2) {
                        if (resident.hasTown()) {
                            if (resident.getTown().hasNation()) {
                                if (resident.isKing()) {
                                    if (TownyUniverse.getInstance().getDataSource().getTown(args[1]) != null) {
                                        Town town = TownyUniverse.getInstance().getDataSource().getTown(args[1]);

                                        if (TownWarBase.getTownWarfromTowny(resident.getTown().getNation(), town).getUuid() == null) {
                                            Confirmation.runOnAccept(() -> {
                                                try {
                                                    twDeclareEvent declareEvent = new twDeclareEvent(resident.getTown().getNation(), town);
                                                    FeudalismMain.plugin.getServer().getPluginManager().callEvent(declareEvent);
                                                } catch (NotRegisteredException e) {
                                                    e.printStackTrace();
                                                }
                                            }).setTitle("Are you sure to want to declare war on " + args[1] + "?").runOnCancel(() -> {sender.sendMessage(ChatInfo.color("&cConfirmation Denied."));}).sendTo(sender);
                                        } else {sender.sendMessage(ChatInfo.color("&cYou're already at war with " + args[1]));}
                                    } else {sender.sendMessage(ChatInfo.color("&c" + args[1] + " isn't registered"));}
                                } else {sender.sendMessage(ChatInfo.color("&cYou aren't the king of the Nation"));}
                            } else {sender.sendMessage(ChatInfo.color("&cYou don't have a nation"));}
                        } else {sender.sendMessage(ChatInfo.color("&cYou don't have a town"));}
                    } else {sender.sendMessage(ChatInfo.color("&cNot enough arguments."));}
                    break;

            }

        } catch (NotRegisteredException e) {
            e.printStackTrace();
            sender.sendMessage(ChatInfo.color("&c" + args[1] + " isn't registered"));
        }
    }
}
