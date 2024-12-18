package com.spyg.needs.config;

import java.util.UUID;

import com.spyg.needs.SpygNeeds;
import com.spygstudios.spyglib.yamlmanager.YamlManager;

public abstract class DataSave extends YamlManager {

    public DataSave(UUID uuid) {
        super("data/" + uuid.toString() + ".yml", SpygNeeds.getInstance());
    }

}
