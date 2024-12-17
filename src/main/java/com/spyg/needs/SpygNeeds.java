package com.spyg.needs;

import org.bukkit.plugin.java.JavaPlugin;

import com.spyg.needs.config.Config;
import com.spyg.needs.listeners.CommandListener;

public class SpygNeeds extends JavaPlugin {

    private static SpygNeeds instance;
    private Config config;

    public void onEnable() {
        instance = this;
        config = new Config(this);
        new CommandListener(this, "spygneeds");

        getLogger().info("<plugin> v. <version> plugin has been enabled!".replace("<plugin>", getName()).replace("<version>", getPluginMeta().getVersion()));
    }

    public void onDisable() {
        getLogger().info("<plugin> v. <version> plugin has been disabled!".replace("<plugin>", getName()).replace("<version>", getPluginMeta().getVersion()));
    }

    public Config getConf() {
        return config;
    }

    public static SpygNeeds getInstance() {
        return instance;
    }

}
