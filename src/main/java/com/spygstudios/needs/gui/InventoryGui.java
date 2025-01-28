package com.spygstudios.needs.gui;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.needs.config.GuisConfig;
import com.spygstudios.needs.needs.PlayerNeeds;
import com.spygstudios.spyglib.color.TranslateColor;

import lombok.Getter;

public class InventoryGui {
    public static void open(Player player) {
        SpygNeeds plugin = SpygNeeds.getInstance();
        GuisConfig config = plugin.getGuisConfig();

        PlayerNeeds playerNeeds = PlayerNeeds.getPlayerNeeds(player.getUniqueId());
        Inventory inventory = player.getServer().createInventory(new InventoryGuiHolder(player), 54, TranslateColor.translate(config.getString("inventory.title")));

        for (Map.Entry<Material, Integer> entry : playerNeeds.getInventory().entrySet()) {
            if (entry.getValue() > 0) {
                if (entry.getValue() > 64) {
                    int amount = entry.getValue();
                    while (amount > 64) {
                        ItemStack item = new ItemStack(entry.getKey(), 64);
                        inventory.addItem(item);
                        amount -= 64;
                    }
                    ItemStack item = new ItemStack(entry.getKey(), amount);
                    inventory.addItem(item);
                } else {
                    ItemStack item = new ItemStack(entry.getKey(), entry.getValue());
                    inventory.addItem(item);
                }
            }

        }

        player.openInventory(inventory);
    }

    public static class InventoryGuiHolder implements InventoryHolder {

        @Getter
        private final Player player;

        public InventoryGuiHolder(Player player) {
            this.player = player;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

    }
}
