package com.ecnu.websocketDemo.entity;

import com.ecnu.websocketDemo.Utils.WebSocketUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlayRoom {
    private String prId;
    private Long startTime;
    private Long playTime;
    private List<String> players;
    private Integer difficulty;

    //可能需要存储游戏信息

    public PlayRoom(List<String> players, Integer difficulty) {
        this.players = players;
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
        prId = "PR" + UUID.randomUUID().toString().substring(0, 5);
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public Long getPlayTime() {
        return System.currentTimeMillis() - startTime;
    }

    public void setPlayTime(Long playTime) {
        this.playTime = playTime;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "PlayRoom{" +
                "prId='" + prId + '\'' +
                ", PlayTime=" + playTime +
                ", players=" + players +
                ", difficulty=" + difficulty +
                '}';
    }
}
