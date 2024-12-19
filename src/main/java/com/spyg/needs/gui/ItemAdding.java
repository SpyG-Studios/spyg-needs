package com.spyg.needs.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.GuisConfig;
import com.spygstudios.spyglib.color.TranslateColor;

import lombok.Getter;

public class ItemAdding {
    public static void open(Player player, String material, int amount, String requester) {
        GuisConfig config = SpygNeeds.getInstance().getGuisConfig();
        SpygNeeds plugin = SpygNeeds.getInstance();
        Inventory inventory = player.getServer().createInventory(new ItemAddingHolder(player, material, amount, requester), 27, TranslateColor.translate(config.getString("item_adding.title").replace("%material%", material).replace("%amount%", String.valueOf(amount)).replace("%requester%", plugin.getNameFromUUID(requester))));

        player.openInventory(inventory);
    }

    public static class ItemAddingHolder implements InventoryHolder {

        @Getter
        private final Player player;

        @Getter
        private final String material;

        @Getter
        private final int amount;

        @Getter
        private final String requester;

        public ItemAddingHolder(Player player, String material, int amount, String requester) {
            this.player = player;
            this.material = material;
            this.amount = amount;
            this.requester = requester;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

    }
}
