package com.ecnu.websocketDemo.entity;

import com.ecnu.websocketDemo.Utils.GameTask;
import com.ecnu.websocketDemo.Utils.JSONUtil;
import com.ecnu.websocketDemo.Utils.WebSocketUtil;

import java.util.*;

public class PlayRoom {
    private String prId;
    private Long startTime;
    private Long playTime;
    private List<String> players;
    private Integer difficulty;

    //存储局内状态，用于重连时向客户端发送当前对局状态
    private Map<String, String> gameMessage = new HashMap<>();

    public PlayRoom(List<String> players, Integer difficulty) {
        this.players = players;
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
        this.prId = "PR" + UUID.randomUUID().toString().substring(0, 5);

        //新建房间时表示游戏开始，加上计时器
        Timer timer= new Timer();
        timer.schedule(new GameTask(this), 100000);
    }

    public Map<String, String> getGameMessage() {
        return gameMessage;
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
