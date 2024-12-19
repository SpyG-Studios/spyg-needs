package com.spyg.needs.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.Message;
import com.spyg.needs.gui.MainGui;

public class CommandListener implements CommandExecutor, Listener {

    public CommandListener(SpygNeeds plugin, String command) {
        plugin.getCommand(command).setExecutor(this);
        plugin.getCommand(command).setTabCompleter(new TabListener());
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            Message.MUST_RUN_BY_PLAYER.sendMessage(sender);
            return true;
        }

        if (args.length == 0) {
            MainGui.open(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("spygneeds.reload")) {
                Message.NO_PERMISSION.sendMessage(player);
                return true;
            }

            SpygNeeds.getInstance().getConf().reloadConfig();
            SpygNeeds.getInstance().getGuisConfig().reloadConfig();
            Message.CONFIG_RELOADED.sendMessage(player);
            return true;
        }

        return true;
    }

}
