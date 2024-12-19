package com.spyg.needs.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.checkerframework.checker.units.qual.s;

import com.spyg.needs.SpygNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public class Config extends YamlManager {

    public Config(SpygNeeds plugin) {
        super("config.yml", plugin);

        set("prefix", "&8[&6SpygNeeds&8] &7»&r");

        set("settings.broadcast-need.actionbar", true, Arrays.asList("Broadcast a message to the action bar if someone added a need."));
        set("settings.broadcast-need.chat", true, Arrays.asList("Broadcast a message to the chat if someone added a need."));

        set("settings.item_adding-list.type", "whitelist", Arrays.asList("Type of the adding list", "Possible values: whitelist | blacklist", "Whitelist: Only items in the list can be added to the needs list.", "Blacklist: All items can be added to the needs list except the ones in the list."));
        set("settings.item_adding-list.items", Arrays.asList("apple", "gold_ore"), Arrays.asList("Items that can be added to the needs list."));

        saveConfig();
    }

    public String getItemListType() {
        return getString("settings.item_adding-list.type");
    }

    public List<Material> getItemListItems() {
        List<Material> items = new ArrayList<>();
        for (String item : getStringList("settings.item_adding-list.items")) {
            try {
                items.add(Material.getMaterial(item.toUpperCase()));
            } catch (IllegalArgumentException e) {
                SpygNeeds.getInstance().getLogger().warning("Invalid item name in " + getItemListType() + " " + item);
                continue;
            }
        }
        return items;
    }

}
