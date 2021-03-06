package com.shmozo.slither.objects;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.utils.BaseUtils;
import com.shmozo.slither.utils.Manager;
import org.bukkit.ChatColor;
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
    private short color;
    private World world;
    private Player player;
    private int smallFoods;
    private int largeFoods;
    private int boostCount;
    private boolean boostColors;
    private double headY;
    private ChatColor chatColor;
    private double bodyY;
    private boolean isAlive;

    /* To be stored in SQL. (So we can add them and say total size, highest size etc)
    uuid,
    name,
    totalScore,
    totalSize,
    totalSmallFoods,
    totalLargeFoods,
    totalBoostTime,
    highestScore,
    highestSize,
    highestSmallFoods,
    highestLargeFoods,
    highestBoostTime
     */

    public SlitherPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.playerScore = 100;
        this.playerSize = 1;
        this.followingArmorStands = new ArrayList<>();
        this.scoreboard = null;
        this.color = BaseUtils.getRandomColor();
        this.chatColor = BaseUtils.getChatColor(getColor());
        this.world = player.getWorld();
        this.player = player;
        this.smallFoods = 0;
        this.largeFoods = 0;
        this.boostCount = 0;
        this.boostColors = false;
        SlitherIO.getInstance().getSlitherList().add(this);
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

    public short getColor() {
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

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public double getBodyY() {
        return bodyY;
    }

    public double getHeadY() {
        return headY;
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
        addPlayerScore(5);
        smallFoods++;
        if (smallFoods % 7 == 0) {
            addFollowingArmorStand();
        }
    }

    public void eatLargeFood() {
        addPlayerScore(15);
        largeFoods++;
        if (largeFoods % 3 == 0) {
            addFollowingArmorStand();
        }
    }

    public void spawnSnake() {  
        isAlive = true;
        playerSize = 1;
        smallFoods = 0;
        largeFoods = 0;
        boostCount = 0;
        boostColors = false;
        headY = 0;
        bodyY = 0;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2));

        /**
         *  SNAKE HEAD
         */
        ArmorStand headStand = getWorld().spawn(BaseUtils.getBlockBehindLocation(getPlayer().getEyeLocation().subtract(0, 2.4, 0)), ArmorStand.class);
        headStand.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, getColor()));
        headStand.setVisible(false);
        headStand.setBasePlate(false);
        headStand.setHeadPose(new EulerAngle(3.14, 3.14, 3.14));
        headStand.setGravity(false);
        headY = headStand.getLocation().getY();
        getFollowingArmorStands().add(headStand);
        /**
         *  SNAKE HEAD
         */

        ArmorStand armorStand = getWorld().spawn(BaseUtils.getBlockBehindLocation(getPlayer().getEyeLocation().subtract(0, 2, 0)), ArmorStand.class);
        armorStand.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, getColor()));
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setHeadPose(new EulerAngle(3.14, 3.14, 3.14));
        bodyY = armorStand.getLocation().getY();
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
            Location location = BaseUtils.getBlockBehindLocation(getFollowingArmorStands().get(getFollowingArmorStands().size() - 1).getLocation());
            location.setY(bodyY);
            ArmorStand armorStand = getWorld().spawn(location, ArmorStand.class);
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
        this.playerScore = 100;
        this.playerSize = 1;
        this.smallFoods = 0;
        this.largeFoods = 0;
        this.boostCount = 0;
        this.boostColors = false;
        for (ArmorStand armorStand : getFollowingArmorStands()) {
            ItemStack itemStack = getWorld().dropItem(armorStand.getEyeLocation(), new ItemStack(Material.STAINED_CLAY, 1, getColor())).getItemStack();
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            armorStand.remove();
        }
        this.color = BaseUtils.getRandomColor();
        this.chatColor = BaseUtils.getChatColor(getColor());
        this.getFollowingArmorStands().clear();
        Manager.openMenu(getPlayer());
    }
}
