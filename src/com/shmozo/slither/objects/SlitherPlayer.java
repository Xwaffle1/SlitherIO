package com.shmozo.slither.objects;

import com.shmozo.slither.SlitherIO;
import com.shmozo.slither.utils.BaseUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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
        this.playerScore = 0;
        this.playerSize = 1;
        this.followingArmorStands = new ArrayList<>();
        this.scoreboard = null;
        this.color = (byte) SlitherIO.getInstance().getRandom().nextInt(15);
        this.world = player.getWorld();
        this.player = player;
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
        if (playerScore % 50 == 0) {
            addFollowingArmorStand();
        }
    }

    public void removePlayerSize(int amount) {
        playerSize -= amount;
    }

    public void removePlayerScore(int amount) {
        playerScore -= amount;
    }

    public void spawnSnake() {
        isAlive = true;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2));
        ArmorStand armorStand = (ArmorStand) getWorld().spawnEntity(BaseUtils.getBlockBehindEntity(getPlayer()), EntityType.ARMOR_STAND);
        armorStand.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, getColor()));
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);
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
            ArmorStand armorStand = (ArmorStand) getWorld().spawnEntity(BaseUtils.getBlockBehindEntity(getFollowingArmorStands().get(getFollowingArmorStands().size() - 1)), EntityType.ARMOR_STAND);
            armorStand.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, getColor()));
            armorStand.setVisible(false);
            armorStand.setSmall(true);
            armorStand.setBasePlate(false);
            armorStand.setHeadPose(new EulerAngle(3.14, 3.14, 3.14));
            getFollowingArmorStands().add(armorStand);
            addPlayerSize(1);
        }
    }

    public void removeFollowingArmorStand() {
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void killPlayer() {
        isAlive = false;
        this.playerScore = 0;
        this.playerSize = 1;
        for (ArmorStand armorStand : getFollowingArmorStands()) {
            ItemStack itemStack = getWorld().dropItemNaturally(armorStand.getEyeLocation(), new ItemStack(Material.STAINED_CLAY, 1, getColor())).getItemStack();
            itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            armorStand.remove();
        }
        this.color = (byte) SlitherIO.getInstance().getRandom().nextInt(15);
        this.getFollowingArmorStands().clear();
    }
}
