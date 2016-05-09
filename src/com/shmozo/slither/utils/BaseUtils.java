package com.shmozo.slither.utils;

import com.shmozo.slither.SlitherIO;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        ItemStack itemStack = new ItemStack(Material.STAINED_CLAY, 1, (byte) SlitherIO.getInstance().getRandom().nextInt(15));
        if (SlitherIO.getInstance().getRandom().nextFloat() >= 0.75) {
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        }
        return itemStack;
    }

    public static Location getBlockBehindLocation(Location location) {
        Vector inverseDirectionVec = location.getDirection().multiply(-1);
        return location.add(inverseDirectionVec);
    }

    public static void sendActionBar(Player player, String message) {
        try {
            Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + SlitherIO.getInstance().getNmsVers() + ".entity.CraftPlayer");
            Object p = c1.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + SlitherIO.getInstance().getNmsVers() + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + SlitherIO.getInstance().getNmsVers() + ".Packet");
            if ((SlitherIO.getInstance().getNmsVers().equalsIgnoreCase("v1_8_R1") || !SlitherIO.getInstance().getNmsVers().startsWith("v1_8_")) && !SlitherIO.getInstance().getNmsVers().startsWith("v1_9_")) {
                Class<?> c2 = Class.forName("net.minecraft.server." + SlitherIO.getInstance().getNmsVers() + ".ChatSerializer");
                Class<?> c3 = Class.forName("net.minecraft.server." + SlitherIO.getInstance().getNmsVers() + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 = Class.forName("net.minecraft.server." + SlitherIO.getInstance().getNmsVers() + ".ChatComponentText");
                Class<?> c3 = Class.forName("net.minecraft.server." + SlitherIO.getInstance().getNmsVers() + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            }
            Method m1 = c1.getDeclaredMethod("getHandle");
            Object h = m1.invoke(p);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
