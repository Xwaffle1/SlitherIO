package com.shmozo.slither.objects;

import com.shmozo.slither.SlitherIO;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

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

    public SlitherPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.playerScore = 0;
        this.playerSize = 1;
        this.followingArmorStands = new ArrayList<>();
        scoreboard = null;
        color = (byte) SlitherIO.getInstance().getRandom().nextInt(15);
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

    public void addFollowingArmorStand() {
        //TODO: Create an armor stand template then add that.
    }

    public void removeFollowingArmorStand() {
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void killPlayer() {
        this.playerScore = 0;
        this.playerSize = 1;
        color = (byte) SlitherIO.getInstance().getRandom().nextInt(15);
        //TODO: Loop through armor stands, drop a clay block below them.
        this.getFollowingArmorStands().clear();
    }
}
