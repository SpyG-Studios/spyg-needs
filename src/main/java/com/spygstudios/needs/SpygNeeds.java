package com.spygstudios.needs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import com.spygstudios.needs.config.Config;
import com.spygstudios.needs.config.DataSave;
import com.spygstudios.needs.config.GuisConfig;
import com.spygstudios.needs.config.Message;
import com.spygstudios.needs.gui.ItemRequesting;
import com.spygstudios.needs.gui.MainGui;
import com.spygstudios.needs.gui.ItemAdding.ItemAddingHolder;
import com.spygstudios.needs.gui.ItemRequesting.ItemRequestingHolder;
import com.spygstudios.needs.gui.MainGui.MainGuiHolder;
import com.spygstudios.needs.listeners.CommandListener;
import com.spygstudios.needs.listeners.InventoryClickListener;
import com.spygstudios.needs.listeners.InventoryCloseListener;
import com.spygstudios.needs.listeners.PlayerChatListener;

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
        List<Object> guis = new ArrayList<>() {
            {
                add(ItemAddingHolder.class);
                add(MainGuiHolder.class);
                add(ItemRequestingHolder.class);
            }
        };
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
            if (guis.contains(holder.getClass())) {
                player.closeInventory();
            }
        }

        getLogger().info("<plugin> v. <version> plugin has been disabled!".replace("<plugin>", getName()).replace("<version>", getPluginMeta().getVersion()));
    }

    public String getNameFromUUID(String uuid) {
        return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
    }

}
