package com.shmozo.slither.utils;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.objects.SlitherPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.*;

import java.util.Collections;

/**
 * Created by Kieran Quigley (Proxying) on 01-May-16 for CherryIO.
 */
public class Manager {

    public static void updateActionBars() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlitherIO.getInstance(), () -> Bukkit.getOnlinePlayers().stream().forEach(player -> {
            SlitherPlayer sPlayer = SlitherIO.getInstance().getSlitherPlayers().get(player.getName());
            if (sPlayer.isAlive()) {
                BaseUtils.sendActionBar(player, ChatColor.AQUA + "Score" + ChatColor.GRAY + ": " + ChatColor.GREEN + sPlayer.getPlayerScore()
                        + ChatColor.GRAY + " || " + ChatColor.AQUA + "Size" + ChatColor.GRAY + ": " + ChatColor.GREEN + sPlayer.getPlayerSize());
            } else {
                BaseUtils.sendActionBar(player, ChatColor.RED + "Dead");
            }
        }), 0L, 1L);
    }

    public static void updateScoreboards() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlitherIO.getInstance(), () -> {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Bukkit.getOnlinePlayers().stream().forEach(player -> {
                SlitherPlayer sPlayer = SlitherIO.getInstance().getSlitherPlayers().get(player.getName());
                Scoreboard scoreboard;
                Objective objective;
                if (sPlayer.getScoreboard() != null) {
                    scoreboard = SlitherIO.getInstance().getSlitherPlayers().get(player.getName()).getScoreboard();
                    objective = scoreboard.getObjective("SlitherBoard");
                } else {
                    scoreboard = manager.getNewScoreboard();
                    objective = scoreboard.registerNewObjective("SlitherBoard", "SlitherBoard");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    sPlayer.setScoreboard(scoreboard);
                }
                objective.setDisplayName(ChatColor.AQUA + "Leaders");
                int max = 5;
                if (SlitherIO.getInstance().getSlitherList().size() < 5) {
                    max = SlitherIO.getInstance().getSlitherList().size();
                }
                scoreboard.getEntries().forEach(scoreboard::resetScores);
                for (int i = 0; i < max; i++) {
                    if (SlitherIO.getInstance().getSlitherList().get(i).isAlive()) {
                        objective.getScore(SlitherIO.getInstance().getSlitherList().get(i).getPlayerName()).setScore(SlitherIO.getInstance().getSlitherList().get(i).getPlayerScore());
                    }
                }
                player.setScoreboard(scoreboard);
            });
        }, 0L, 15L);
    }

    public static void updateTopPlayerList() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlitherIO.getInstance(), () -> Collections.sort(SlitherIO.getInstance().getSlitherList(), (SlitherPlayer sP1, SlitherPlayer sP2) -> sP1.getPlayerScore() - sP2.getPlayerScore()), 0L, 20L);
    }

    public static void handlePlayerMovement() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SlitherIO.getInstance(), () -> Bukkit.getOnlinePlayers().stream().forEach(player -> {
            SlitherPlayer sPlayer = SlitherIO.getInstance().getSlitherPlayers().get(player.getName());
            if (sPlayer.isAlive()) {
                if (player.isOnGround()) {
                    if (player.isSneaking()) {
                        if (sPlayer.getPlayerScore() > 25) {
                            sPlayer.boost();
                            player.setVelocity(player.getLocation().getDirection().multiply(.7));
                        } else {
                            sPlayer.disableBoostColors();
                            player.setVelocity(player.getLocation().getDirection().multiply(.4));
                        }
                    } else {
                        sPlayer.disableBoostColors();
                        player.setVelocity(player.getLocation().getDirection().multiply(.4));
                    }
                }
            }
        }), 0, 1);
    }

    public static void openMenu(Player player){
        Inventory inv = Bukkit.createInventory(null, 9, "Menu");
        inv.setItem(3, new ItemBuilder().setItem(Material.STAINED_GLASS_PANE, DyeColor.LIME.getDyeData(), "Yes", new String[]{}).build());
        inv.setItem(5, new ItemBuilder().setItem(Material.STAINED_GLASS_PANE, DyeColor.RED.getDyeData(), "No", new String[]{}).build());
        player.openInventory(inv);
    }

    public static void loginTasks(Player player) {
        SlitherIO.getInstance().getSlitherPlayers().put(player.getName(), new SlitherPlayer(player));
    }

    public static void logoutTasks(Player player) {
        SlitherPlayer sP = SlitherIO.getInstance().getSlitherPlayers().get(player.getName());
        sP.getFollowingArmorStands().stream().forEach(ArmorStand::remove);
        SlitherIO.getInstance().getSlitherList().remove(sP);
        SlitherIO.getInstance().getSlitherPlayers().remove(player.getName());
    }
}
