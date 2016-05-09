package com.shmozo.slither.listeners;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by chase on 5/8/2016.
 */
public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Restart?")) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();
            if (event.getRawSlot() == 3) {
                Manager.loginTasks(p);
            } else if (event.getRawSlot() == 5) {
                event.getWhoClicked().closeInventory();
            }
        }
    }

}
