package com.shmozo.slither.listeners;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.objects.SlitherPlayer;
import com.shmozo.slither.utils.BaseUtils;
import com.shmozo.slither.utils.Manager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

/**
 * Created by Kieran Quigley (Proxying) on 01-May-16 for CherryIO.
 */
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSprintToggle(PlayerToggleSprintEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() != null && event.getRightClicked() instanceof ArmorStand) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerEatItem(PlayerPickupItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        event.setCancelled(true);
        if (!SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).isAlive()) return;
        event.getItem().remove();
        if (BaseUtils.isSlitherFood(event.getItem().getItemStack())) {
            if (BaseUtils.isLargeFood(event.getItem().getItemStack())) {
                //Add large food points
                SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).eatLargeFood();
            } else {
                SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).eatSmallFood();
                //Add small food points
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCollideArmorStand(PlayerMoveEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (!SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).isAlive()) return;
        if (event.getPlayer().getNearbyEntities(0.5, 0.5, 0.5).stream().anyMatch(ArmorStand.class::isInstance)) {
            ArmorStand stand = (ArmorStand) event.getPlayer().getNearbyEntities(0.5, 0.5, 0.5).stream().filter(e -> e instanceof ArmorStand).findFirst().get();
            if (!SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).getFollowingArmorStands().contains(stand)) {
                //Player has collided with another player's tail and should be killed.
                SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).killPlayer();
            }
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (!SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).isAlive()) return;
        SlitherPlayer sPlayer = SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName());
        for (int i = (sPlayer.getFollowingArmorStands().size() - 1); i >= 0; i--) {
            ArmorStand stand = sPlayer.getFollowingArmorStands().get(i);
            if (i == 0) {
                Location loc = event.getPlayer().getLocation().clone();
                loc.setY(event.getPlayer().getEyeLocation().getY());
                stand.teleport(loc);
            } else {
                Location loc = sPlayer.getFollowingArmorStands().get((i - 1)).getLocation().clone();
                loc.setY(event.getPlayer().getLocation().getY());
                stand.teleport(loc);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void setupPlayerObject(PlayerJoinEvent event) {
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        Manager.loginTasks(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cleanPlayerObjectQuit(PlayerQuitEvent event) {
        Manager.logoutTasks(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cleanPlayerObjectKick(PlayerKickEvent event) {
        Manager.logoutTasks(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
