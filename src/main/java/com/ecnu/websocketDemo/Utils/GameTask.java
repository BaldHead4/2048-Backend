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
        WebSocketUtil.playRoomGroupSending(JSONUtil.buildTextJSONObject("时间到，游戏结束").toJSONString(), this.playRoom);
        WebSocketUtil.playRoomGroupSending(JSONUtil.buildTextJSONObject("已退出房间 " + playRoom.getPrId() + "，请重新匹配").toJSONString(), this.playRoom);
        WebSocketUtil.playRoomList.remove(this.playRoom);
        for (String playerId : this.playRoom.getPlayers()) {
            /*如果房间中的玩家没有掉线，则将该玩家的关联的游戏房间置为 null */
            WebSocketConnect connect = WebSocketUtil.webSocketSet.get(playerId);
            if (connect != null) {
                connect.setPlayRoom(null);
            }
        }
    }
}
