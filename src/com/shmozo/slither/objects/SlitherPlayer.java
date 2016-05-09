package com.shmozo.slither.objects;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.utils.BaseUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Kieran Quigley (Proxying) on 01-May-16 for CherryIO.
 */
public class SlitherPlayer {

    private UUID uuid;
    private String playerName;
    private int playerScore;
    private int playerSize;
    private List<ArmorStand> followingArmorStands;
    private Scoreboard scoreboard;
    private byte color;
    private World world;
    private Player player;
    private int smallFoods;
    private int largeFoods;
    private int boostCount;
    private boolean boostColors;

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    private boolean isAlive;

    public SlitherPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.playerScore = 100;
        this.playerSize = 1;
        this.followingArmorStands = new ArrayList<>();
        this.scoreboard = null;
        this.color = (byte) SlitherIO.getInstance().getRandom().nextInt(15);
        this.world = player.getWorld();
        this.player = player;
        this.smallFoods = 0;
        this.largeFoods = 0;
        this.boostCount = 0;
        this.boostColors = false;
        spawnSnake();
    }

    public int getPlayerSize() {
        return playerSize;
    }

    public List<ArmorStand> getFollowingArmorStands() {
        return followingArmorStands;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public byte getColor() {
        return color;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public void addPlayerSize(int amount) {
        playerSize += amount;
    }

    public void addPlayerScore(int amount) {
        playerScore += amount;
    }

    public void removePlayerSize(int amount) {
        playerSize -= amount;
    }

    public void removePlayerScore(int amount) {
        playerScore -= amount;
    }

    public void boost() {
        removePlayerScore(5);
        boostCount++;
        enableBoostColors();
        if (boostCount % 4 == 0) {
            getWorld().dropItem(getFollowingArmorStands().get(getFollowingArmorStands().size() - 1).getEyeLocation(), new ItemStack(Material.STAINED_CLAY, 1, getColor())).getItemStack();
        } else if (boostCount % 10 == 0) {
            removeFollowingArmorStand();
        }
    }

    public void enableBoostColors() {
        if (!boostColors) {
            ItemStack helmet = (new ItemStack(Material.STAINED_CLAY, 1, getColor()));
            helmet.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            getFollowingArmorStands().stream().forEach(armorStand -> armorStand.setHelmet(helmet));
            boostColors = true;
        }
    }

    public void disableBoostColors() {
        if (boostColors) {
            ItemStack helmet = (new ItemStack(Material.STAINED_CLAY, 1, getColor()));
            getFollowingArmorStands().stream().forEach(armorStand -> armorStand.setHelmet(helmet));
            boostColors = false;
        }
    }
    public void eatSmallFood() {
        addPlayerScore(20);
        smallFoods++;
        if (smallFoods % 5 == 0) {
            addFollowingArmorStand();
        }
    }

    public void eatLargeFood() {
        addPlayerScore(50);
        largeFoods++;
        if (largeFoods % 2 == 0) {
            addFollowingArmorStand();
        }
    }

    public void spawnSnake() {  
        isAlive = true;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2));

        /**
         *  SNAKE HEAD
         */
        Location loc = player.getLocation().clone();
        loc.setY(player.getEyeLocation().getY());
        ArmorStand headStand = getWorld().spawn(BaseUtils.getBlockBehindEntity(getPlayer()).subtract(0, 0.3, 0), ArmorStand.class);
        headStand.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, getColor()));
        headStand.setVisible(false);
        headStand.setBasePlate(false);
        headStand.setHeadPose(new EulerAngle(3.14, 3.14, 3.14));
        headStand.setMarker(true);
        getFollowingArmorStands().add(headStand);
        /**
         *  SNAKE HEAD
         */

        ArmorStand armorStand = getWorld().spawn(BaseUtils.getBlockBehindEntity(getPlayer()).subtract(0, 0.3, 0), ArmorStand.class);
        armorStand.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, getColor()));
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setHeadPose(new EulerAngle(3.14, 3.14, 3.14));
        getFollowingArmorStands().add(armorStand);
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
        addFollowingArmorStand();
    }

    public void addFollowingArmorStand() {
        if (!getFollowingArmorStands().isEmpty()) {
            ArmorStand armorStand = getWorld().spawn(BaseUtils.getBlockBehindEntity(getFollowingArmorStands().get(getFollowingArmorStands().size() - 1)).subtract(0, 0.3, 0), ArmorStand.class);
            armorStand.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, getColor()));
            armorStand.setVisible(false);
            armorStand.setSmall(true);
            armorStand.setBasePlate(false);
            armorStand.setGravity(false);
            armorStand.setHeadPose(new EulerAngle(3.14, 3.14, 3.14));
            getFollowingArmorStands().add(armorStand);
            addPlayerSize(1);
        }
    }

    public void removeFollowingArmorStand() {
        if (!getFollowingArmorStands().isEmpty()) {
            ArmorStand armorStand =  getFollowingArmorStands().get(getFollowingArmorStands().size() - 1);
            getWorld().dropItem(armorStand.getEyeLocation(), new ItemStack(Material.STAINED_CLAY, 1, getColor())).getItemStack();
            getFollowingArmorStands().remove(armorStand);
            armorStand.remove();
            removePlayerSize(1);
        }
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void killPlayer() {
        isAlive = false;
        this.playerScore = 0;
        this.playerSize = 1;
        this.smallFoods = 0;
        this.largeFoods = 0;
        this.boostCount = 0;
        for (ArmorStand armorStand : getFollowingArmorStands()) {
            ItemStack itemStack = getWorld().dropItem(armorStand.getEyeLocation(), new ItemStack(Material.STAINED_CLAY, 1, getColor())).getItemStack();
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            armorStand.remove();
        }
        this.color = (byte) SlitherIO.getInstance().getRandom().nextInt(15);
        this.getFollowingArmorStands().clear();
    }
}
