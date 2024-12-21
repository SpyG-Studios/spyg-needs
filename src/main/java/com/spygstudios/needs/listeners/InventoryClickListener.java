package com.spygstudios.needs.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.needs.config.Message;
import com.spygstudios.needs.gui.ItemAdding;
import com.spygstudios.needs.gui.ItemRequesting;
import com.spygstudios.needs.gui.MainGui;
import com.spygstudios.needs.gui.ItemRequesting.ItemRequestingHolder;
import com.spygstudios.needs.gui.MainGui.MainGuiHolder;
import com.spygstudios.spyglib.persistentdata.PersistentData;

public class InventoryClickListener implements Listener {

    private static SpygNeeds plugin;

    public InventoryClickListener(SpygNeeds plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin = SpygNeeds.getInstance();
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
        PersistentData data = new PersistentData(plugin, event.getCurrentItem());

        String action = data.getString("action");

        if (action == null) {
            return;
        }

        event.setCancelled(true);

        Player player = ((MainGuiHolder) event.getInventory().getHolder()).getPlayer();

        switch (action) {
        case "refresh":
            int currentPage = data.getInt("page");
            MainGui.open(player, currentPage);
            break;

        case "previous_page":
            player.closeInventory();
            currentPage = data.getInt("page");
            int totalPages = data.getInt("total_pages");

            if (currentPage <= 1) {
                currentPage = totalPages;
            } else {
                currentPage--;
            }

            MainGui.open(player, currentPage);
            break;

        case "next_page":
            currentPage = data.getInt("page");
            totalPages = data.getInt("total_pages");

            if (currentPage >= totalPages) {
                currentPage = 1;
            } else {
                currentPage++;
            }

            MainGui.open(player, currentPage);
            break;

        case "close":
            player.closeInventory();
            break;

        case "request":
            ItemRequesting.open(player);
            break;
        case "give":

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
