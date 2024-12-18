package com.spyg.needs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.spyg.needs.config.Config;
import com.spyg.needs.config.GuisConfig;
import com.spyg.needs.config.Message;
import com.spyg.needs.listeners.CommandListener;
import com.spyg.needs.listeners.InventoryClickListener;
import com.spyg.needs.listeners.InventoryCloseListener;
import com.spyg.needs.listeners.PlayerChatListener;
import com.spyg.needs.needs.PlayerNeeds;

import lombok.Getter;

public class SpygNeeds extends JavaPlugin {

    @Getter
    private static SpygNeeds instance;

    @Getter
    private Config conf;

    @Getter
    private GuisConfig guisConfig;

    public void onEnable() {
        instance = this;
        conf = new Config(this);
        guisConfig = new GuisConfig(this);
        new CommandListener(this, "spygneeds");

        new InventoryClickListener(instance);
        new InventoryCloseListener(instance);
        new PlayerChatListener(instance);

        Bukkit.getOnlinePlayers().forEach((player) -> {
            new PlayerNeeds(player);
        });

        Message.init(conf);

        getLogger().info("<plugin> v. <version> plugin has been enabled!".replace("<plugin>", getName()).replace("<version>", getPluginMeta().getVersion()));
    }

    public void onDisable() {
        getLogger().info("<plugin> v. <version> plugin has been disabled!".replace("<plugin>", getName()).replace("<version>", getPluginMeta().getVersion()));
    }

}
