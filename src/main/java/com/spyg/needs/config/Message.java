package com.spyg.needs.config;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spyg.needs.SpygNeeds;

import net.kyori.adventure.text.Component;

public enum Message {
    PREFIX("prefix", "&8[&6SpygNeeds&8] &7»&r"), MUST_RUN_BY_PLAYER("must-run-by-player", "&c%prefix% This command must be run by a player."), NO_PERMISSION("no-permission", "%prefix% &cYou do not have permission to do this."),

    GUI_MAIN_TITLE("guis.main.title", "&6Player Needs");

    private String node;
    private String defaultMessage;

    private Message(String node, String defaultMessage) {
        this.node = node;
        this.defaultMessage = defaultMessage;

        setDefault();
    }

    public static void sendMessage(Player player, Message message) {
        player.sendMessage(message.get());
    }

    public static void sendMessage(CommandSender sender, Message message) {
        sender.sendMessage(message.get());
    }

    public String getNode() {
        return node;
    }

    public String getRaw() {
        return SpygNeeds.getInstance().getConf().getString("messages." + node);
    }

    public Component get() {
        return SpygNeeds.getInstance().getConf().getMessage(node);
    }

    public void setDefault() {
        if (SpygNeeds.getInstance().getConf().getString("messages." + node) == null) {
            SpygNeeds.getInstance().getConf().set("messages." + node, defaultMessage);
        }
        SpygNeeds.getInstance().getConf().saveConfig();
    }

    public static void init() {
        Bukkit.getLogger().info("Initializing messages...");
    }
}
