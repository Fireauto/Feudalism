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

import java.text.SimpleDateFormat;

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
                                            }).setTitle("Are you sure to want to declare war on " + args[1] + "?").runOnCancel(() -> sender.sendMessage(ChatInfo.prefix("&cConfirmation Denied."))).sendTo(sender);
                                        } else {sender.sendMessage(ChatInfo.prefix("&cYou're already at war with " + args[1]));}
                                    } else {sender.sendMessage(ChatInfo.prefix("&c" + args[1] + " isn't registered"));}
                                } else {sender.sendMessage(ChatInfo.prefix("&cYou aren't the king of the Nation"));}
                            } else {sender.sendMessage(ChatInfo.prefix("&cYou don't have a nation"));}
                        } else {sender.sendMessage(ChatInfo.prefix("&cYou don't have a town"));}
                    } else {sender.sendMessage(ChatInfo.prefix("&cNot enough arguments."));}
                    break;
                case "war":
                    if (args.length == 2) {
                        if (resident.hasTown()) {
                            if (resident.getTown().hasNation()) {
                                Town town = TownyUniverse.getInstance().getDataSource().getTown(args[1]);

                                if (TownWarBase.getTownWarfromTowny(resident.getTown().getNation(), town) != null) {
                                    TownWar war = TownWarBase.getTownWarfromTowny(resident.getTown().getNation(), town);
                                    String string1 = "&7<<>> &6&l" + resident.getTown().getNation().getName().toUpperCase() + " &7vs &6&l" + town.getName().toUpperCase() + " &7<<>>";
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");

                                    ChatInfo.sendCenteredMessage(sender.getServer().getPlayer(sender.getName()), ChatInfo.color(string1));
                                    sender.sendMessage(ChatInfo.color("&eStarted:&6 " + sdf.format(war.getTime())));
                                    sender.sendMessage(ChatInfo.color("&eAggressor:&6 " + war.getNation()));
                                    sender.sendMessage(ChatInfo.color("&eDefender:&6 " + war.getTown()));
                                    sender.sendMessage(ChatInfo.color("&eWarscore:&6 " + war.getWarscore()));
                                } else {sender.sendMessage(ChatInfo.prefix("&cYou aren't at war with " + town.getName()));}
                            } else {

                            }
                        } else {sender.sendMessage(ChatInfo.prefix("&cYou don't have a town"));}
                    } else {sender.sendMessage(ChatInfo.prefix("&cNot enough arguments."));}
                    break;

            }

        } catch (NotRegisteredException e) {
            e.printStackTrace();
            sender.sendMessage(ChatInfo.prefix("&c" + args[1] + " isn't registered"));
        }
    }
}
