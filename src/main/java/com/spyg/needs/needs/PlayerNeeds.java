package com.spyg.needs.needs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.spyg.needs.SpygNeeds;
import com.spyg.needs.config.Config;
import com.spyg.needs.config.DataSave;
import com.spyg.needs.config.Message;
import com.spygstudios.spyglib.broadcast.BroadcastMessage;

import lombok.Getter;

public class PlayerNeeds extends DataSave {

    @Getter
    private static final List<PlayerNeeds> needs = new ArrayList<>();

    private static final Config config = SpygNeeds.getInstance().getConf();

    @Getter
    private Map<Material, Integer> items = new HashMap<Material, Integer>();

    @Getter
    private OfflinePlayer requester;

    public PlayerNeeds(UUID requester) {
        super(requester);

        this.requester = Bukkit.getOfflinePlayer(requester);

        ConfigurationSection needsSection = getConfigurationSection("needs");

        needs.add(this);

        if (needsSection == null) {
            return;
        }

        for (String key : needsSection.getKeys(false)) {
            System.out.println("1. Loading need: " + key + " " + needsSection.getInt(key));
            items.put(Material.getMaterial(key), needsSection.getInt(key));
        }

    }

    public void addNeed(Material material, int amount) {

        System.out.println("2. Current needs");
        for (Material mat : items.keySet()) {
            System.out.println(mat.name() + " " + items.get(mat));
        }

        System.out.println("3. Adding need: " + material.name() + " " + amount);
        items.put(material, amount);

        System.out.println("4. Current needs after addition");
        for (Material mat : items.keySet()) {
            System.out.println(mat.name() + " " + items.get(mat));
        }

        if (config.getBoolean("settings.broadcast-need.chat", true)) {
            BroadcastMessage.chat(Message.BROADCAST_ADDED_NEED_CHAT.getRaw(), Map.of("%player%", requester.getName(), "%item%", material.name(), "%prefix%", config.getPrefix(), "%amount%", String.valueOf(amount)));
        }

        if (config.getBoolean("settings.broadcast-need.actionbar", true)) {
            BroadcastMessage.actionBar(Message.BROADCAST_ADDED_NEED_ACTIONBAR.getRaw(), Map.of("%player%", requester.getName(), "%item%", material.name(), "%prefix%", config.getPrefix(), "%amount%", String.valueOf(amount)));
        }

        System.out.println("STATIC NEEDS SIZE: " + needs.size());

        save();
    }

    public void save() {

        System.out.println("5. Saving needs");
        for (Material material : items.keySet()) {

            System.out.println("Saving need: " + material.name() + " " + items.get(material));
            overwriteSet("needs." + material.name(), items.get(material));
        }

        saveConfig();
    }

    public static PlayerNeeds getPlayerNeeds(Player player) {
        for (PlayerNeeds need : needs) {
            if (need.getRequester().equals(player)) {
                return need;
            }
        }
        return new PlayerNeeds(player.getUniqueId());
    }

}
