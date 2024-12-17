package com.spyg.needs.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabListener implements TabCompleter {

    private List<String> commands = Arrays.asList("");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return commands;
        }

        return null;
    }
}
