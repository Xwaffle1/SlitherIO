package com.shmozo.slither.listeners;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.utils.BaseUtils;
import com.shmozo.slither.utils.Manager;
import org.bukkit.GameMode;
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
    public void onPlayerEatItem(PlayerPickupItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        event.setCancelled(true);
        event.getItem().remove();
        if (BaseUtils.isSlitherFood(event.getItem().getItemStack())) {
            if (BaseUtils.isLargeFood(event.getItem().getItemStack())) {
                //Add large food points
            } else {
                //Add small food points
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCollideArmorStand(PlayerMoveEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
        if (event.getPlayer().getNearbyEntities(0.5, 0.5, 0.5).stream().anyMatch(ArmorStand.class::isInstance)) {
            //Player has collided with another player's tail and should be killed.
            SlitherIO.getInstance().getSlitherPlayers().get(event.getPlayer().getName()).killPlayer();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void setupPlayerObject(PlayerJoinEvent event) {
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
