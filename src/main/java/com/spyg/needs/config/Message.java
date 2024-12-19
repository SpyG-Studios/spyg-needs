package com.spyg.needs.config;

import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spygstudios.spyglib.color.TranslateColor;
import com.spygstudios.spyglib.components.ComponentUtils;

import net.kyori.adventure.text.Component;

public enum Message {

    MUST_RUN_BY_PLAYER("must-run-by-player", "&c%prefix% This command must be run by a player."),

    NO_PERMISSION("no-permission", "%prefix% &cYou do not have permission to do this."),

    CANT_GIVE_YOURSELF("cant-give-yourself", "%prefix% &cYou can't give yourself items."),

    WRONG_ITEMS_GIVEN("wrong-items-given", "%prefix% &cYou have added wrong items. &f%player%&c needs &f%item%&c."),

    YOU_HAVE_GIVEN("you-have-given", "%prefix% &aYou have given &f%amount% %item% &ato &f%player%&a."),

    ENTER_AMOUNT_INVALID("enter-amount-invalid", "%prefix% &cInvalid amount (&f%entered%&c). Please enter a valid positive number."),

    ENTER_AMOUNT_CANCELLED("enter-amount-cancelled", "%prefix% &cYou have cancelled the process."),

    ENTER_AMOUNT("enter-amount", "%prefix% &aEnter the amount of &f%item% &ayou need &7&o(Enter a &f-&7&o to cancel):"),

    BROADCAST_ADDED_NEED_CHAT("broadcast-added-need-chat", "%prefix% &a%player% is needing for &f%item%&a!"),

    BROADCAST_ADDED_NEED_ACTIONBAR("broadcast-added-need-actionbar", "&a%player% is needing for &f%item%&a!"),

    CONFIG_RELOADED("config-reloaded", "%prefix% &aConfiguration reloaded.");

    private String node;
    private String defaultMessage;
    private List<String> defaultMessages;
    private static Config config;

    private Message(String node, String defaultMessage) {
        this.node = node;
        this.defaultMessage = defaultMessage;
    }

    private Message(String node, List<String> defaultMessage) {
        this.node = node;
        this.defaultMessages = defaultMessage;
    }

    public void sendMessage(Player player) {
        sendMessage((CommandSender) player);
    }

    public void sendMessage(CommandSender sender) {
        if (defaultMessages != null) {
            for (String message : defaultMessages) {
                sender.sendMessage(TranslateColor.translate(message));
            }
            return;
        }
        sender.sendMessage(get());
    }

    public void sendMessage(Player player, Map<String, String> placeholders) {
        sendMessage((CommandSender) player, placeholders);
    }

    public void sendMessage(CommandSender sender, Map<String, String> placeholders) {

        if (defaultMessages != null) {
            for (String message : defaultMessages) {
                sender.sendMessage(ComponentUtils.replaceComponent(TranslateColor.translate(message), placeholders));
            }
            return;
        }

        sender.sendMessage(ComponentUtils.replaceComponent(get(), placeholders));
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
