package com.spyg.needs.config;

import java.util.Arrays;

import com.spyg.needs.SpygNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public class GuisConfig extends YamlManager {

    public GuisConfig(SpygNeeds plugin) {
        super("guis.yml", plugin);

        set("item_requesting.title", "&7Place the item that you need.");
        set("item_requesting.filleritem.name", "-");
        set("item_requesting.filleritem.material", "GRAY_STAINED_GLASS_PANE");
        set("item_requesting.filleritem.lore", Arrays.asList("&r"));

        set("main.title", "&7Player needs");
        set("main.items.refresh.name", "&6Refresh");
        set("main.items.refresh.material", "PAPER");
        set("main.items.refresh.slot", 47);
        set("main.items.refresh.lore", Arrays.asList("&r", "&7Click to refresh the list."));

        set("main.items.close.name", "&cClose");
        set("main.items.close.material", "BARRIER");
        set("main.items.close.slot", 49);
        set("main.items.close.lore", Arrays.asList("&r", "&7Click to close the GUI."));

        set("main.items.request.name", "&aRequest an Item");
        set("main.items.request.material", "LIME_CANDLE");
        set("main.items.request.slot", 51);
        set("main.items.request.lore", Arrays.asList("&r", "&7Click to request an item."));

        saveConfig();

    }

}
