package com.spyg.needs.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.Message;
import com.spyg.needs.gui.ItemAdding;
import com.spyg.needs.gui.ItemRequesting;
import com.spyg.needs.gui.MainGui;
import com.spyg.needs.gui.ItemRequesting.ItemRequestingHolder;
import com.spyg.needs.gui.MainGui.MainGuiHolder;
import com.spygstudios.spyglib.persistentdata.PersistentData;

public class InventoryClickListener implements Listener {

    public InventoryClickListener(SpygNeeds plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getClickedInventory() == null) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }

        if (event.getInventory().getHolder() instanceof MainGuiHolder) {
            mainGui(event);
            return;
        }

        if (event.getInventory().getHolder() instanceof ItemRequestingHolder) {
            requestingGui(event);
            return;
        }

    }

    private void requestingGui(InventoryClickEvent event) {

        int slot = event.getSlot();

        if (slot != 13 && event.getClickedInventory().getHolder() instanceof ItemRequestingHolder) {
            event.setCancelled(true);
        }

    }

    private void mainGui(InventoryClickEvent event) {
        PersistentData data = new PersistentData(SpygNeeds.getInstance(), event.getCurrentItem());

        String action = data.getString("action");

        if (action == null) {
            return;
        }

        event.setCancelled(true);

        Player player = ((MainGuiHolder) event.getInventory().getHolder()).getPlayer();

        switch (action) {
        case "refresh":
            player.closeInventory();
            MainGui.open(player);
            break;

        case "close":
            player.closeInventory();
            break;

        case "request":
            player.closeInventory();
            ItemRequesting.open(player);
            break;
        case "give":
            player.closeInventory();

            String material = data.getString("material");
            int amount = data.getInt("amount");
            String requester = data.getString("requester");

            if (requester.equals(player.getUniqueId().toString())) {
                Message.CANT_GIVE_YOURSELF.sendMessage(player);
                return;
            }

            ItemAdding.open(player, material, amount, requester);
            break;

        default:
            break;
        }
    }

}
