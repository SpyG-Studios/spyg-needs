package com.spygstudios.needs.listeners;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.needs.config.Message;
import com.spygstudios.needs.gui.InventoryGui.InventoryGuiHolder;
import com.spygstudios.needs.gui.ItemAdding.ItemAddingHolder;
import com.spygstudios.needs.gui.ItemRequesting.ItemRequestingHolder;
import com.spygstudios.needs.needs.PendingNeed;
import com.spygstudios.needs.needs.PlayerNeeds;
import com.spygstudios.spyglib.inventory.InventoryUtils;

public class InventoryCloseListener implements Listener {

    public InventoryCloseListener(SpygNeeds plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getInventory().getHolder() instanceof InventoryGuiHolder) {
            inventoryGui(event);
            return;
        }

        if (event.getInventory().getHolder() instanceof ItemRequestingHolder) {
            itemRequesting(event);
            return;
        }

        if (event.getInventory().getHolder() instanceof ItemAddingHolder) {
            itemAdding(event);
            return;
        }

    }

    private void inventoryGui(InventoryCloseEvent event) {

        PlayerNeeds playerNeeds = PlayerNeeds.getPlayerNeeds(((InventoryGuiHolder) event.getInventory().getHolder()).getPlayer().getUniqueId());

        Map<Material, Integer> inventory = playerNeeds.getInventory();
        inventory.clear();
        for (ItemStack item : event.getInventory().getContents()) {
            if (item == null) {
                continue;
            }

            Material material = item.getType();
            int amount = item.getAmount();

            if (inventory.containsKey(material)) {
                playerNeeds.addInventory(material, amount);
            }
        }

        playerNeeds.setChanged(true);
    }

    private void itemAdding(InventoryCloseEvent event) {

        ItemStack[] contents = event.getInventory().getContents();

        Material material = Material.getMaterial(((ItemAddingHolder) event.getInventory().getHolder()).getMaterial());

        boolean hasTheMaterial = true;

        for (ItemStack item : contents) {

            if (item == null) {
                continue;
            }

            if (item.getType() == material) {
                hasTheMaterial = false;
                break;
            }

            if (item.getType() != material) {
                hasTheMaterial = true;
            }

        }

        Player player = ((ItemAddingHolder) event.getInventory().getHolder()).getPlayer();
        int amountRequested = ((ItemAddingHolder) event.getInventory().getHolder()).getAmount();
        int givenAmount = InventoryUtils.countItems(event.getInventory(), material);
        OfflinePlayer requester = Bukkit.getOfflinePlayer(UUID.fromString(((ItemAddingHolder) event.getInventory().getHolder()).getRequester()));

        if (hasTheMaterial) {
            Message.WRONG_ITEMS_GIVEN.sendMessage(player, Map.of("%item%", material.name(), "%amount%", String.valueOf(amountRequested), "%player%", requester.getName()));
            for (ItemStack item : contents) {
                if (item == null) {
                    continue;
                }

                player.getInventory().addItem(item);
            }
            return;
        }

        PlayerNeeds playerNeeds = PlayerNeeds.getPlayerNeeds(requester.getUniqueId());

        if (givenAmount > amountRequested) {
            int rest = givenAmount - amountRequested;
            player.getInventory().addItem(new ItemStack(material, rest));
            givenAmount = amountRequested;
        }

        playerNeeds.addInventory(material, givenAmount);

        Message.YOU_HAVE_GIVEN.sendMessage(player, Map.of("%player%", requester.getName(), "%amount%", String.valueOf(givenAmount), "%material%", material.name(), "%item%", material.name()));

        PlayerNeeds giver = PlayerNeeds.getPlayerNeeds(player.getUniqueId());

        giver.addItemsGivenOut(givenAmount);
        playerNeeds.addItemsReceived(givenAmount);

        for (ItemStack item : contents) {
            if (item == null) {
                continue;
            }

            if (item.getType() != material) {
                player.getInventory().addItem(item);
            }
        }
    }

    private void itemRequesting(InventoryCloseEvent event) {
        ItemStack centerItem = event.getInventory().getItem(13);

        if (centerItem == null) {
            return;
        }

        String itemListType = SpygNeeds.getInstance().getConf().getItemListType();
        boolean itemListing = SpygNeeds.getInstance().getConf().isItemListEnabled();
        Player player = ((ItemRequestingHolder) event.getInventory().getHolder()).getPlayer();
        player.getInventory().addItem(centerItem);

        if (itemListing) {
            if (itemListType.equalsIgnoreCase("whitelist")) {
                if (!SpygNeeds.getInstance().getConf().getItemListItems().contains(centerItem.getType())) {
                    Message.ITEM_BLOCKED.sendMessage(player);
                    return;
                }
            }

            if (itemListType.equalsIgnoreCase("blacklist")) {
                if (SpygNeeds.getInstance().getConf().getItemListItems().contains(centerItem.getType())) {
                    Message.ITEM_BLOCKED.sendMessage(player);
                    return;
                }
            }
        }

        new PendingNeed(player, centerItem.getType());
        player.getInventory().addItem(centerItem);
    }

}
