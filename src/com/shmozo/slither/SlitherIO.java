package com.shmozo.slither;

import com.shmozo.slither.listeners.PlayerListener;
import com.shmozo.slither.objects.SlitherPlayer;
import com.shmozo.slither.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Kieran Quigley (Proxying) on 01-May-16 for CherryIO.
 */
public class SlitherIO extends JavaPlugin {

    private static SlitherIO instance = null;

    private static Random random = new Random();

    private Map<String, SlitherPlayer> slitherPlayers = new HashMap<>();

    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        Manager.updatePlayerScoreboards();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getOnlinePlayers().stream().forEach(player -> {
            SlitherPlayer sPlayer = SlitherIO.getInstance().getSlitherPlayers().get(player.getName());
            if (sPlayer.isAlive()) {
                if (player.isOnGround()) {
                    if (player.isSneaking()) {
                        player.setVelocity(player.getLocation().getDirection().multiply(.5));
                    } else {
                        player.setVelocity(player.getLocation().getDirection().multiply(.3));
                    }
                }
            }
        }), 0, 1);
    }

    public void onDisable() {

    }

    public static SlitherIO getInstance() {
        return instance;
    }

    public Map<String, SlitherPlayer> getSlitherPlayers() {
        return slitherPlayers;
    }

    public Random getRandom() {
        return random;
    }
}
