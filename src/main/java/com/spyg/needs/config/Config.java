package com.spyg.needs.config;

import java.util.Arrays;

import com.spyg.needs.SpygNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public class Config extends YamlManager {

    public Config(SpygNeeds plugin) {
        super("config.yml", plugin);

        set("prefix", "&8[&6SpygNeeds&8] &7»&r");

        set("settings.broadcast-need.actionbar", true, Arrays.asList("Broadcast a message to the action bar if someone added a need."));
        set("settings.broadcast-need.chat", true, Arrays.asList("Broadcast a message to the chat if someone added a need."));

        saveConfig();
    }

}
