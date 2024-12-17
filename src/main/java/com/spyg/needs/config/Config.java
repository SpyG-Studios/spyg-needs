package com.spyg.needs.config;

import com.spyg.needs.SpygNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public class Config extends YamlManager {

    public Config(SpygNeeds plugin) {
        super("config.yml", plugin);

        set("prefix", "&8[&6SpygNeeds&8] &7»&r");

        saveConfig();
    }

}
