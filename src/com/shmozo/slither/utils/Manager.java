package com.shmozo.slither.utils;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.objects.SlitherPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlitherIO.getInstance(), () -> Bukkit.getOnlinePlayers().stream().forEach(player -> {
            SlitherPlayer sPlayer = SlitherIO.getInstance().getSlitherPlayers().get(player.getName());
            Scoreboard scoreboard;
            Objective objective;
            if (sPlayer.isAlive()) {
                BaseUtils.sendActionBar(player, ChatColor.AQUA + "Score" + ChatColor.GRAY + ": " + ChatColor.GREEN + sPlayer.getPlayerScore()
                        + ChatColor.GRAY + " || " + ChatColor.AQUA + "Size" + ChatColor.GRAY + ": " + ChatColor.GREEN + sPlayer.getPlayerSize());
            } else {
                BaseUtils.sendActionBar(player, ChatColor.RED + "Dead");
            }
            if (sPlayer.getScoreboard() != null) {
                scoreboard = SlitherIO.getInstance().getSlitherPlayers().get(player.getName()).getScoreboard();
                objective = scoreboard.getObjective("SlitherBoard");
            } else {
                scoreboard = manager.getNewScoreboard();
                objective = scoreboard.registerNewObjective("SlitherBoard", "SlitherBoard");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(ChatColor.AQUA + "Leaders");
                sPlayer.setScoreboard(scoreboard);
            }
            //TODO: Loop through all SlitherPlayers, sort by getScore then display top 5-10?

            player.setScoreboard(scoreboard);

        }), 0L, 1L);
    }

    public static void handlePlayerMovement() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlitherIO.getInstance(), () -> Bukkit.getOnlinePlayers().stream().forEach(player -> {
            SlitherPlayer sPlayer = SlitherIO.getInstance().getSlitherPlayers().get(player.getName());
            if (sPlayer.isAlive()) {
                if (player.isOnGround()) {
                    if (player.isSneaking()) {
                        if (sPlayer.getPlayerScore() > 25) {
                            sPlayer.removePlayerScore(5);
                            player.setVelocity(player.getLocation().getDirection().multiply(.5));
                        } else {
                            player.setVelocity(player.getLocation().getDirection().multiply(.3));
                        }
                    } else {
                        player.setVelocity(player.getLocation().getDirection().multiply(.3));
                    }
                }
            }
        }), 0, 1);
    }

    public static void loginTasks(Player player) {
        SlitherIO.getInstance().getSlitherPlayers().put(player.getName(), new SlitherPlayer(player));
    }

    public static void logoutTasks(Player player) {
        SlitherIO.getInstance().getSlitherPlayers().get(player.getName()).getFollowingArmorStands().stream().forEach(ArmorStand::remove);
        SlitherIO.getInstance().getSlitherPlayers().remove(player.getName());
    }
}
