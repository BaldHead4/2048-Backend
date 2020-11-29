package com.ecnu.websocketDemo.entity;

import com.ecnu.websocketDemo.Utils.GameTask;
import com.ecnu.websocketDemo.Utils.JSONUtil;
import com.ecnu.websocketDemo.Utils.WebSocketUtil;
import com.ecnu.websocketDemo.controller.WebSocketConnect;
import com.sun.crypto.provider.PBEWithMD5AndDESCipher;

import java.util.*;

public class PlayRoom {
    private String prId;
    private Long startTime;
    private Long playTime;
    private List<String> players;
    private Integer difficulty;

    //存储局内状态，用于重连时向客户端发送当前对局状态
    private Map<String, String> gameMessage = new HashMap<>();

    //存储游戏结束的玩家 id，用于当所有玩家结束时使游戏提前结束
    private List<String> failList = new ArrayList<>();

    private Map<String, String> idToName = new HashMap<>();


    //初始化游戏房间
    public PlayRoom(List<String> players, Integer difficulty) {
        this.players = players;
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
        this.prId = "PR" + UUID.randomUUID().toString().substring(0, 5);
        for (String player : players) {
            idToName.put(player, WebSocketUtil.webSocketSet.get(player).getUsername());
        }

        //新建房间时表示游戏开始，加上计时器
        Timer timer= new Timer();
        timer.schedule(new GameTask(this), 300000);
    }

    public Map<String, String> getIdToName() {
        return idToName;
    }

    public List<String> getFailList() {
        return failList;
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

    public void gameOver() {
        WebSocketUtil.playRoomGroupSending(JSONUtil.buildQuitJSONObject("已退出房间 " + this.prId + "，请重新匹配")
                .toJSONString(), this);
        WebSocketUtil.playRoomList.remove(this);
        for (String playerId : this.players) {
            /*如果房间中的玩家没有掉线，则将该玩家的关联的游戏房间置为 null */
            WebSocketConnect connect = WebSocketUtil.webSocketSet.get(playerId);
            if (connect != null) {
                connect.setPlayRoom(null);
            }
        }
    }
}
