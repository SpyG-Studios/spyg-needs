package com.spyg.needs.config;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spyg.needs.SpygNeeds;
import com.spygstudios.spyglib.color.TranslateColor;

import net.kyori.adventure.text.Component;

public enum Message {
    PREFIX("prefix", "&8[&6SpygNeeds&8] &7»&r"), MUST_RUN_BY_PLAYER("must-run-by-player", "&cThis command must be run by a player."), NO_PERMISSION("no-permission", "&cYou do not have permission to do this.");

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
        return TranslateColor.translate(getRaw());
    }

    public void setDefault() {
        if (SpygNeeds.getInstance().getConf().getString("messages." + node) == null) {
            SpygNeeds.getInstance().getConf().set("messages." + node, defaultMessage);
        }
    }
}
