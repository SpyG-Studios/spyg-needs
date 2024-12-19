package com.spygstudios.needs.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabListener implements TabCompleter {

    private List<String> commands = Arrays.asList("info");
    private Map<String, String> commandsWithPermissions = new HashMap<String, String>() {
        {
            put("reload", "spygneeds.reload");
        }
    };

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>(commands);

        for (String command : commandsWithPermissions.keySet()) {
            if (sender.hasPermission(commandsWithPermissions.get(command))) {
                completions.add(command);
            }
        }

        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String command : completions) {
                if (command.startsWith(args[0])) {
                    result.add(command);
                }
            }
            return result;
        }

        return null;
    }
}
