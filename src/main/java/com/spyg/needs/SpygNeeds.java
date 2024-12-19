package com.spyg.needs;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.spyg.needs.config.Config;
import com.spyg.needs.config.DataSave;
import com.spyg.needs.config.GuisConfig;
import com.spyg.needs.config.Message;
import com.spyg.needs.listeners.CommandListener;
import com.spyg.needs.listeners.InventoryClickListener;
import com.spyg.needs.listeners.InventoryCloseListener;
import com.spyg.needs.listeners.PlayerChatListener;
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

        DataSave.loadAll();
        DataSave.startSavingTask();

        Message.init(conf);

        getLogger().info("<plugin> v. <version> plugin has been enabled!".replace("<plugin>", getName()).replace("<version>", getPluginMeta().getVersion()));
    }

    public void onDisable() {
        DataSave.saveAll();
        getLogger().info("<plugin> v. <version> plugin has been disabled!".replace("<plugin>", getName()).replace("<version>", getPluginMeta().getVersion()));
    }

    public String getNameFromUUID(String uuid) {
        return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
    }

}
