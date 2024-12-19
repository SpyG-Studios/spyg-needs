package com.spygstudios.needs.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.needs.config.GuisConfig;
import com.spygstudios.needs.needs.PlayerNeeds;
import com.spygstudios.spyglib.color.TranslateColor;
import com.spygstudios.spyglib.item.ItemUtils;
import com.spygstudios.spyglib.persistentdata.PersistentData;
import com.spygstudios.spyglib.placeholder.ParseListPlaceholder;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public class MainGui {

    public static void open(Player player, int page) {
        GuisConfig config = SpygNeeds.getInstance().getGuisConfig();

        ConfigurationSection itemsSection = config.getConfigurationSection("main.items");
        if (itemsSection == null) {
            throw new IllegalArgumentException("Main GUI items section is missing in the config file.");
        }

        String neededSlots = config.getString("main.needed-slots");
        if (neededSlots == null) {
            throw new IllegalArgumentException("Needed slots are missing in the main GUI section.");
        }

        int startSlot;
        int endSlot;
        try {
            String[] parts = neededSlots.split("-");
            startSlot = Integer.parseInt(parts[0]);
            endSlot = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid needed slots in the main GUI section. Must be in the format 'min-max'. Example: '0-44'");
        }

        List<PlayerNeeds> allNeeds = PlayerNeeds.getNeeds();

        int itemsPerPage = endSlot - startSlot + 1;

        List<ItemWithNeed> allItems = new ArrayList<>();
        for (PlayerNeeds need : allNeeds) {
            for (Map.Entry<Material, Integer> entry : need.getItems().entrySet()) {
                allItems.add(new ItemWithNeed(entry, need));
            }
        }

        int totalNeeds = allItems.size();
        if (totalNeeds == 0) {
            totalNeeds = 1;
        }

        int totalPages = (int) Math.ceil((double) totalNeeds / (double) itemsPerPage);

        if (page > totalPages) {
            page = totalPages;
        }

        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allItems.size());

        List<ItemWithNeed> itemsOnPage = allItems.subList(fromIndex, toIndex);

        Component title = TranslateColor.translate(config.getString("main.title").replace("%current_page%", String.valueOf(page)).replace("%total_pages%", String.valueOf(totalPages)));

        Inventory inventory = player.getServer().createInventory(new MainGuiHolder(player), 54, title);

        int slotIndex = startSlot;
        for (ItemWithNeed itemWithNeed : itemsOnPage) {
            if (slotIndex > endSlot) {
                break;
            }

            Material material = itemWithNeed.getItem().getKey();
            int amount = itemWithNeed.getItem().getValue();
            PlayerNeeds need = itemWithNeed.getNeed();

            String materialName = config.getString("main.need.material").replace("%material%", material.name());
            String displayname = config.getString("main.need.name").replace("%player%", need.getRequester().getName()).replace("%amount%", String.valueOf(amount));
            List<String> lore = ParseListPlaceholder.parse(config.getStringList("main.need.lore"), Map.of("%player%", need.getRequester().getName(), "%amount%", String.valueOf(amount)));

            int itemAmount = Math.min(amount, 64);
            ItemStack item = ItemUtils.create(materialName, displayname, lore, itemAmount);
            PersistentData data = new PersistentData(SpygNeeds.getInstance(), item);

            data.set("action", "give");
            data.set("material", material.name());
            data.set("amount", amount);
            data.set("requester", need.getRequester().getUniqueId().toString());
            data.save();

            inventory.setItem(slotIndex, item);
            slotIndex++;
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
                List<String> lore = itemSection.getStringList("lore");
                ItemStack item = ItemUtils.create(material, name, lore);

                PersistentData data = new PersistentData(SpygNeeds.getInstance(), item);
                data.set("action", key);
                data.set("page", page);
                data.set("total_pages", totalPages);

                data.save();
                inventory.setItem(slot, item);
            }
        }

        player.openInventory(inventory);
    }

    public static class ItemWithNeed {
        private final Map.Entry<Material, Integer> item;
        private final PlayerNeeds need;

        public ItemWithNeed(Map.Entry<Material, Integer> item, PlayerNeeds need) {
            this.item = item;
            this.need = need;
        }

        public Map.Entry<Material, Integer> getItem() {
            return item;
        }

        public PlayerNeeds getNeed() {
            return need;
        }
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
