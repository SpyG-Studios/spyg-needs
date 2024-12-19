package com.spygstudios.needs.needs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.needs.config.Config;
import com.spygstudios.needs.config.DataSave;
import com.spygstudios.needs.config.Message;
import com.spygstudios.spyglib.broadcast.BroadcastMessage;

import lombok.Getter;
import lombok.Setter;

public class PlayerNeeds extends DataSave {

    @Getter
    private static final List<PlayerNeeds> needs = new ArrayList<>();

    private static final Config config = SpygNeeds.getInstance().getConf();

    @Getter
    private Map<Material, Integer> items = new HashMap<Material, Integer>();

    @Getter
    private Map<Material, Integer> inventory = new HashMap<Material, Integer>();

    @Getter
    @Setter
    private boolean isChanged = false;

    @Getter
    private OfflinePlayer requester;

    public PlayerNeeds(UUID requester) {
        super(requester);

        this.requester = Bukkit.getOfflinePlayer(requester);

        ConfigurationSection needsSection = getConfigurationSection("needs");
        ConfigurationSection inventorySection = getConfigurationSection("inventory");

        needs.add(this);

        if (needsSection == null) {
            needsSection = createSection("needs");
            saveConfig();
        }

        if (inventorySection == null) {
            inventorySection = createSection("inventory");
            saveConfig();
        }

        for (String key : inventorySection.getKeys(false)) {
            inventory.put(Material.getMaterial(key), inventorySection.getInt(key));
        }

        for (String key : needsSection.getKeys(false)) {
            items.put(Material.getMaterial(key), needsSection.getInt(key));
        }

        recalculateNeededByInventory();

    }

    public void recalculateNeededByInventory() {

        for (Material need : new HashMap<>(items).keySet()) {
            for (Material material : inventory.keySet()) {

                if (material.equals(need)) {
                    if (inventory.get(material) >= items.get(need)) {
                        items.remove(need);
                    }
                }
            }
        }

        isChanged = true;

    }

    public void addInventory(Material material, int amount) {

        int need = items.get(material);
        if (need > amount) {
            items.put(material, need - amount);
        } else {
            items.remove(material);
        }

        if (inventory.containsKey(material)) {
            inventory.put(material, inventory.get(material) + amount);
        } else {
            inventory.put(material, amount);
        }

        recalculateNeededByInventory();

    }

    public void addNeed(Material material, int amount) {

        items.put(material, amount);

        if (config.getBoolean("settings.broadcast-need.chat", true)) {
            BroadcastMessage.chat(Message.BROADCAST_ADDED_NEED_CHAT.getRaw(),
                    Map.of("%player%", requester.getName(), "%item%", material.name(), "%prefix%", config.getPrefix(), "%amount%", String.valueOf(amount)));
        }

        if (config.getBoolean("settings.broadcast-need.actionbar", true)) {
            BroadcastMessage.actionBar(Message.BROADCAST_ADDED_NEED_ACTIONBAR.getRaw(),
                    Map.of("%player%", requester.getName(), "%item%", material.name(), "%prefix%", config.getPrefix(), "%amount%", String.valueOf(amount)));
        }

        isChanged = true;

    }

    public void save() {

        set("needs", null);
        set("inventory", null);

        for (Material material : items.keySet()) {
            overwriteSet("needs." + material.name(), items.get(material));
        }

        for (Material material : inventory.keySet()) {
            overwriteSet("inventory." + material.name(), inventory.get(material));
        }

        saveConfig();
    }

    public static PlayerNeeds getPlayerNeeds(Player player) {
        return getPlayerNeeds(player.getUniqueId());
    }

    public static PlayerNeeds getPlayerNeeds(UUID uuid) {
        for (PlayerNeeds need : needs) {
            if (need.getRequester().getUniqueId().equals(uuid)) {
                return need;
            }
        }
        return new PlayerNeeds(uuid);
    }

}
