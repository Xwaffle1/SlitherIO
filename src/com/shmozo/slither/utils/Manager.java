package com.shmozo.slither.utils;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.objects.SlitherPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * Created by Kieran Quigley (Proxying) on 01-May-16 for CherryIO.
 */
public class Manager {

    private static ScoreboardManager manager;

    public static void updatePlayerScoreboards() {
        manager = Bukkit.getScoreboardManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlitherIO.getInstance(), () -> Bukkit.getOnlinePlayers().stream().filter(player -> player != null).forEach(player -> {
            Scoreboard scoreboard;
            Objective objective;
            if (SlitherIO.getInstance().getSlitherPlayers().get(player.getName()).getScoreboard() != null) {
                scoreboard = SlitherIO.getInstance().getSlitherPlayers().get(player.getName()).getScoreboard();
                objective = scoreboard.getObjective("SlitherBoard");
            } else {
                scoreboard = manager.getNewScoreboard();
                objective = scoreboard.registerNewObjective("SlitherBoard", "SlitherBoard");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(ChatColor.AQUA + "Leaders");
                SlitherIO.getInstance().getSlitherPlayers().get(player.getName()).setScoreboard(scoreboard);
            }
            //TODO: Loop through all SlitherPlayers, sort by getScore then display top 5-10?

            player.setScoreboard(scoreboard);
        }), 200L, 15L);
    }

    public static void loginTasks(Player player) {
        SlitherIO.getInstance().getSlitherPlayers().put(player.getName(), new SlitherPlayer(player));
    }

    public static void logoutTasks(Player player) {
        SlitherIO.getInstance().getSlitherPlayers().remove(player.getName());
    }
}
