package com.shmozo.slither.utils;

import com.shmozo.slither.SlitherIO;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Created by Kieran Quigley (Proxying) on 01-May-16 for CherryIO.
 */
public class BaseUtils {

    public static boolean isSlitherFood(ItemStack itemStack) {
        return itemStack.getType() == Material.STAINED_CLAY;
    }

    public static boolean isLargeFood(ItemStack itemStack) {
        return !itemStack.getEnchantments().isEmpty();
    }

    public static ItemStack createRandomSlitherFood() {
        ItemStack itemStack = new ItemStack(Material.STAINED_CLAY, (byte) SlitherIO.getInstance().getRandom().nextInt(15));
        if (SlitherIO.getInstance().getRandom().nextFloat() >= 0.75) {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        }
        return itemStack;
    }

    public static Location getBlockBehindEntity(Entity entity) {
        Vector inverseDirectionVec = entity.getLocation().getDirection().normalize().multiply(-1);
        return entity.getLocation().add(inverseDirectionVec);
    }
}
