package com.ecnu.websocketDemo.Utils;

import com.ecnu.websocketDemo.controller.WebSocketConnect;
import com.ecnu.websocketDemo.entity.PlayRoom;

import java.util.TimerTask;


/*实现游戏对局的计时器*/
public class GameTask extends TimerTask {

    private PlayRoom playRoom;

    public GameTask(PlayRoom playRoom) {
        this.playRoom = playRoom;
    }

    @Override
    public void run() {
        /*游戏结束时，向房间内玩家广播游戏结束，并将该房间从 playRoomList 中移除，将房间中玩家关联的游戏房间设为 null */
        WebSocketUtil.playRoomGroupSending(JSONUtil.buildTimeOutJSONObject("时间到，游戏结束").toJSONString(), this.playRoom);
        this.playRoom.gameOver();
    }
}
