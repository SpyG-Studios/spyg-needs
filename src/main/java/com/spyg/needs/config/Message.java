package com.spyg.needs.config;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;

public enum Message {
    PREFIX("prefix", "&8[&6SpygNeeds&8] &7»&r"), 
    MUST_RUN_BY_PLAYER("must-run-by-player", "&c%prefix% This command must be run by a player."),
    NO_PERMISSION("no-permission", "%prefix% &cYou do not have permission to do this."),

    GUI_MAIN_TITLE("guis.main.title", "&6Player Needs");

    private String node;
    private String defaultMessage;
    private static Config config;

    private Message(String node, String defaultMessage) {
        this.node = node;
        this.defaultMessage = defaultMessage;
    }

    public void sendMessage(Player player) {
        player.sendMessage(get());
    }

    public void sendMessage(CommandSender sender) {
        sender.sendMessage(get());
    }

    public String getNode() {
        return node;
    }

    public String getRaw() {
        return config.getString("messages." + node);
    }

    public Component get() {
        return config.getMessage(node);
    }

    public void setDefault() {
        if (config.getString("messages." + node) == null) {
            config.set("messages." + node, defaultMessage);
        }
        config.saveConfig();
    }

    public static void init(Config conf) {
        config = conf;
        for (Message message : Message.values()) {
            message.setDefault();
        }
    }
}
