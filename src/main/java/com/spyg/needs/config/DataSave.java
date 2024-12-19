package com.spyg.needs.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.spyg.needs.SpygNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public abstract class DataSave extends YamlManager {

    private UUID uuid;

    public DataSave(UUID uuid) {
        super("data/" + uuid.toString() + ".yml", SpygNeeds.getInstance());
        this.uuid = uuid;
    }

    public Map<Material, Integer> getItemsInFile() {
        ConfigurationSection needsSection = getConfigurationSection("needs");

        if (needsSection == null) {
            return null;
        }

        Map<Material, Integer> items = new HashMap<Material, Integer>();
        for (String key : needsSection.getKeys(false)) {

            int amount = needsSection.getInt("needs." + key);

            if (amount < 0 || amount == 0) {
                continue;
            }
            items.put(Material.getMaterial(key), amount);
        }

        return items;

    }

}
