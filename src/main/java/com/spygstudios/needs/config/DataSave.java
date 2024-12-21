package com.spygstudios.needs.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.needs.needs.PlayerNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public abstract class DataSave extends YamlManager {

    private static SpygNeeds plugin = SpygNeeds.getInstance();
    private static BukkitTask saveTaskId;

    public DataSave(UUID uuid) {
        super("data/" + uuid.toString() + ".yml", plugin);
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

    public static void loadAll() {
        File dataFolder = new File(plugin.getDataFolder(), "data");
        File[] files = dataFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".yml")) {
                    String name = file.getName().replace(".yml", "");
                    UUID uuid = UUID.fromString(name);
                    new PlayerNeeds(uuid);
                }
            }
        }
    }

    public static void saveAll() {
        for (PlayerNeeds needs : PlayerNeeds.getNeeds()) {
            if (needs.isChanged()) {
                needs.save();
                needs.setChanged(false);
            }
        }
    }

    public static void startSavingTask() {
        saveTaskId = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {

            @Override
            public void run() {
                saveAll();
            }

        }, 0, 20 * 60);
    }

    public static void stopSavingTask() {
        saveTaskId.cancel();
    }

}
