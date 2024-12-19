package com.spyg.needs.gui;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.GuisConfig;
import com.spyg.needs.needs.PlayerNeeds;
import com.spygstudios.spyglib.color.TranslateColor;
import com.spygstudios.spyglib.item.ItemUtils;
import com.spygstudios.spyglib.persistentdata.PersistentData;
import com.spygstudios.spyglib.placeholder.ParseListPlaceholder;

import lombok.Getter;

public class MainGui {

    public static void open(Player player) {
        GuisConfig config = SpygNeeds.getInstance().getGuisConfig();
        Inventory inventory = player.getServer().createInventory(new MainGuiHolder(player), 54, TranslateColor.translate(config.getString("main.title")));

        ConfigurationSection itemsSection = config.getConfigurationSection("main.items");
        if (itemsSection == null) {
            throw new IllegalArgumentException("Main GUI items section is missing in the config file.");
        }

        String neededSlots = config.getString("main.needed-slots");
        if (neededSlots == null) {
            throw new IllegalArgumentException("Needed slots are missing in the main GUI section.");
        }

        int startSlot = 0;
        int endSlot = 0;
        try {
            startSlot = Integer.parseInt(neededSlots.split("-")[0]);
            endSlot = Integer.parseInt(neededSlots.split("-")[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid needed slots in the main GUI section. Must be in the format 'min-max'. Example: '0-44'");
        }

        List<PlayerNeeds> needs = PlayerNeeds.getNeeds();

        int i = startSlot;
        for (PlayerNeeds need : needs) {
            for (Map.Entry<Material, Integer> entry : need.getItems().entrySet()) {
                Material material = entry.getKey();
                int amount = entry.getValue();
                String materialName = config.getString("main.need.material").replace("%material%", material.name());
                String displayname = config.getString("main.need.name").replace("%player%", need.getRequester().getName()).replace("%amount%", String.valueOf(amount));
                List<String> lore = ParseListPlaceholder.parse(config.getStringList("main.need.lore"), Map.of("%player%", need.getRequester().getName(), "%amount%", String.valueOf(amount)));

                int itemAmount = amount > 64 ? 64 : amount;
                ItemStack item = ItemUtils.create(materialName, displayname, lore, itemAmount);
                PersistentData data = new PersistentData(SpygNeeds.getInstance(), item);

                data.set("action", "give");
                data.set("material", material.name());
                data.set("amount", amount);
                data.set("requester", need.getRequester().getUniqueId().toString());

                data.save();

                inventory.setItem(i, item);
                i++;
                if (i == endSlot) {
                    break;
                }
            }
        }

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            if (itemSection != null) {
                int slot = itemSection.getInt("slot", -1);
                if (slot == -1) {
                    throw new IllegalArgumentException("Slot is missing in the main GUI item section.");
                }

                if (slot >= startSlot && slot <= endSlot) {
                    throw new IllegalArgumentException("Slot " + slot + " is already used for the needed items.");
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

        @Getter
        private final Player player;

        public MainGuiHolder(Player player) {
            this.player = player;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

    }

}
