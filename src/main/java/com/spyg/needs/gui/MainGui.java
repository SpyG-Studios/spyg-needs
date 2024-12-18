package com.spyg.needs.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.spyg.needs.config.Message;

public class MainGui {

    public static void open(Player player) {
        Inventory inventory = player.getServer().createInventory(new MainGuiHolder(player), InventoryType.CHEST, Message.GUI_MAIN_TITLE.get());

        player.openInventory(inventory);
    }

    public static class MainGuiHolder implements InventoryHolder {

        private final Player player;

        public MainGuiHolder(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

    }

}
