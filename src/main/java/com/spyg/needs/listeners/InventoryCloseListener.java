package com.spyg.needs.listeners;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.Config;
import com.spyg.needs.config.Message;
import com.spyg.needs.gui.ItemRequesting.ItemRequestingHolder;
import com.spyg.needs.needs.PlayerNeeds;
import com.spyg.needs.needs.PendingNeed;
import com.spygstudios.spyglib.broadcast.BroadcastMessage;

public class InventoryCloseListener implements Listener {

    private Config config;

    public InventoryCloseListener(SpygNeeds plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.config = plugin.getConf();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (!(event.getInventory().getHolder() instanceof ItemRequestingHolder)) {
            return;
        }

        ItemStack centerItem = event.getInventory().getItem(13);

        if (centerItem == null) {
            return;
        }

        Player player = ((ItemRequestingHolder) event.getInventory().getHolder()).getPlayer();
        new PendingNeed(player, centerItem.getType());

    }

}
