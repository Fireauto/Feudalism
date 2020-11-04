package com.github.hafixion.Commands;

import com.github.hafixion.Modules.Ruin.RuinCommands;
import com.github.hafixion.Modules.TownWar.TownWarCommands;
import com.github.hafixion.Utils.ChatInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FeudalismCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String[] newargs = Arrays.copyOfRange(args, 1, args.length);
        if (args.length != 0) {
            switch (args[0]) {
                // ruin commands
                case "ruin":
                    RuinCommands.exec(commandSender, newargs);
                    break;
                case "townwar":
                    TownWarCommands.exec(commandSender, newargs);
                    break;
                default:
                    commandSender.sendMessage(ChatInfo.color("&c" + args[0] + " is not a valid argument."));
                    break;

            }
        } else {
            //todo add plugin info
        }
        return false;
    }

    public static class FeudalismTabCompleter implements TabCompleter {
        public static List<String> tab = new ArrayList<>(Arrays.asList("ruin-townwar".split("-")));

        @Override
        public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
            List<String> result = Collections.singletonList("");

            // args[0] tab completer
            if (strings.length == 1) {
                result = tab;
            }
            return result;
        }
    }
}
