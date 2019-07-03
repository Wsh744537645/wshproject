package com.mpool.account.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket 服务端站点
 */

@Slf4j
@ServerEndpoint("/socket")
@Service
public class WebSocketServiceImpl {

    //当前在线连接数
    private static volatile int onlineCount = 0;
    //存放每个客户端对应的实例
    private static CopyOnWriteArraySet<WebSocketServiceImpl> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新连接加入！当前在线人数为 {}", getOnlineCount());

    }

    /**
     * 关闭连接调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为 {}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        log.info("来自客户端的消息 {}", message);


    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        log.info("发生错误 session = {}", session);
        error.printStackTrace();
    }

    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
    private void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    private static synchronized int getOnlineCount(){
        return onlineCount;
    }

    private static synchronized void addOnlineCount(){
        WebSocketServiceImpl.onlineCount ++;
    }

    private static synchronized void subOnlineCount(){
        WebSocketServiceImpl.onlineCount --;
    }
}
