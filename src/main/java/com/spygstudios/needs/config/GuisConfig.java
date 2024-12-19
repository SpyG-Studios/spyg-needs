package com.spygstudios.needs.config;

import java.util.Arrays;

import com.spygstudios.needs.SpygNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public class GuisConfig extends YamlManager {

    public GuisConfig(SpygNeeds plugin) {
        super("guis.yml", plugin);

        set("item_requesting.title", "&7Place the item that you need.");
        set("item_requesting.filleritem.name", "-");
        set("item_requesting.filleritem.material", "GRAY_STAINED_GLASS_PANE");
        set("item_requesting.filleritem.lore", Arrays.asList("&r"));

        set("item_adding.title", "&7%requester% is requesting %amount% of %material%");

        set("main.title", "&8Player needs &8(&7%current_page%&8/&7%total_pages%&8)");
        set("main.needed-slots", "0-45", Arrays.asList("The slots that will be used to display player's needs."));
        set("main.need.name", "&7%player%'s needs");
        set("main.need.material", "%material%");
        set("main.need.lore", Arrays.asList("&r", "&7Amount: %amount%"));

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

        set("main.items.previous_page.name", "&6Previous Page");
        set("main.items.previous_page.material", "PAPER");
        set("main.items.previous_page.slot", 45);
        set("main.items.previous_page.lore", Arrays.asList("&r", "&7Click to go to the previous page."));

        set("main.items.next_page.name", "&6Next Page");
        set("main.items.next_page.material", "PAPER");
        set("main.items.next_page.slot", 53);
        set("main.items.next_page.lore", Arrays.asList("&r", "&7Click to go to the next page."));

        saveConfig();

    }

}
