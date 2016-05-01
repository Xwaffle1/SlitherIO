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
