package com.shmozo.slither.listeners;

import com.shmozo.slither.SlitherIO;
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
        if (event.getInventory().getTitle().equalsIgnoreCase("Menu")) {
            event.setCancelled(true);
            if (event.getRawSlot() == 3) {
                event.getWhoClicked().teleport(event.getWhoClicked().getWorld().getSpawnLocation());
                SlitherIO.getInstance().getSlitherPlayers().get(event.getWhoClicked().getName()).spawnSnake();
                event.getWhoClicked().closeInventory();
            } else if (event.getRawSlot() == 5) {
                event.getWhoClicked().closeInventory();
            }
        }
    }

}
