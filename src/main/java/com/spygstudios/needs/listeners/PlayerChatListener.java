package com.spygstudios.needs.listeners;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.needs.config.Message;
import com.spygstudios.needs.needs.PendingNeed;
import com.spygstudios.spyglib.components.ComponentUtils;

import io.papermc.paper.event.player.AsyncChatEvent;

public class PlayerChatListener implements Listener {

    public PlayerChatListener(SpygNeeds plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        PendingNeed pendingNeed = PendingNeed.getPendingNeed(player);

        if (pendingNeed == null) {
            return;
        }
        event.setCancelled(true);
        String message = ComponentUtils.fromComponent(event.message());

        if (message.equalsIgnoreCase("-")) {
            Message.ENTER_AMOUNT_CANCELLED.sendMessage(player);
            pendingNeed.cancel();
            return;
        }

        int amount = -1;
        try {
            amount = Integer.parseInt(message);
        } catch (NumberFormatException e) {
            Message.ENTER_AMOUNT_INVALID.sendMessage(player, Map.of("%entered%", message));
            return;
        }

        if (amount < 0) {
            Message.ENTER_AMOUNT_INVALID.sendMessage(player, Map.of("%entered%", message));
            return;
        }

        pendingNeed.create(amount);

    }

}
