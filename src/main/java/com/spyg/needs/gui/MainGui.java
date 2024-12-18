package com.spyg.needs.gui;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.GuisConfig;
import com.spygstudios.spyglib.color.TranslateColor;
import com.spygstudios.spyglib.item.ItemUtils;
import com.spygstudios.spyglib.persistentdata.PersistentData;

public class MainGui {

    public static void open(Player player) {
        GuisConfig config = SpygNeeds.getInstance().getGuisConfig();
        Inventory inventory = player.getServer().createInventory(new MainGuiHolder(player), 54, TranslateColor.translate(config.getString("main.title")));

        ConfigurationSection itemsSection = config.getConfigurationSection("main.items");
        if (itemsSection == null) {
            throw new IllegalArgumentException("Main GUI items section is missing in the config file.");
        }

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            if (itemSection != null) {
                int slot = itemSection.getInt("slot", -1);
                if (slot == -1) {
                    throw new IllegalArgumentException("Slot is missing in the main GUI item section.");
                }
                String material = itemSection.getString("material", "STONE");
                String name = itemSection.getString("name", "-");
                ItemStack item = ItemUtils.create(material, name, itemSection.getStringList("lore"));

                PersistentData data = new PersistentData(SpygNeeds.getInstance(), item);

                data.set("action", key);
                data.save();

                inventory.setItem(slot, item);
            }
        }

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
