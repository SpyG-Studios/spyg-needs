package com.spygstudios.needs.needs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.spygstudios.needs.config.Message;

import lombok.Getter;

public class PendingNeed {

    @Getter
    private static List<PendingNeed> pendingNeeds = new ArrayList<>();

    @Getter
    private Player player;

    @Getter
    private Material material;

    public PendingNeed(Player player, Material material) {
        if (pendingNeeds.contains(this)) {
            return;
        }

        this.player = player;
        this.material = material;

        info();

        pendingNeeds.add(this);

    }

    public void create(int amount) {
        PlayerNeeds playerNeeds = PlayerNeeds.getPlayerNeeds(player);
        System.out.println("1.: " + material.name() + ": " + amount);
        playerNeeds.addNeed(material, amount);

        cancel();
    }

    public void cancel() {
        pendingNeeds.remove(this);
    }

    public void info() {
        Message.ENTER_AMOUNT.sendMessage(player, Map.of("%item%", material.name()));
    }

    public static PendingNeed getPendingNeed(Player player) {
        for (PendingNeed pendingNeed : pendingNeeds) {
            if (pendingNeed.getPlayer().equals(player)) {
                return pendingNeed;
            }
        }
        return null;
    }

}
