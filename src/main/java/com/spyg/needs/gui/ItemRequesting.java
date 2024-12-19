package com.spyg.needs.gui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.GuisConfig;
import com.spygstudios.spyglib.color.TranslateColor;
import com.spygstudios.spyglib.item.ItemUtils;

import lombok.Getter;

public class ItemRequesting {

    public static void open(Player player) {
        GuisConfig config = SpygNeeds.getInstance().getGuisConfig();
        Inventory inventory = player.getServer().createInventory(new ItemRequestingHolder(player), 27, TranslateColor.translate(config.getString("item_requesting.title")));

        String material = config.getString("item_requesting.filleritem.material");
        String name = config.getString("item_requesting.filleritem.name");
        List<String> lore = config.getStringList("item_requesting.filleritem.lore");

        if (material == null) {
            throw new IllegalArgumentException("Material is missing in the item requesting GUI section for filler item.");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name is missing in the item requesting GUI section for filler item.");
        }

        if (lore == null) {
            throw new IllegalArgumentException("Lore is missing in the item requesting GUI section for filler item.");
        }

        ItemStack fillerItem = ItemUtils.create(material, name, lore);
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, fillerItem);
        }

        inventory.setItem(13, new ItemStack(Material.AIR));

        player.openInventory(inventory);
    }

    public static class ItemRequestingHolder implements InventoryHolder {

        @Getter
        private final Player player;

        public ItemRequestingHolder(Player player) {
            this.player = player;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

    }

}
