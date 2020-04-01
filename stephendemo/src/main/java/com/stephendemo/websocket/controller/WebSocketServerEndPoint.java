package com.stephendemo.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author jmfen
 * date 2020-03-26
 */

@Slf4j
@Controller
@ServerEndpoint("/")
public class WebSocketServerEndPoint {


    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        log.info("[onOpen][session({})接入]", session);
    }

    @OnMessage
    public void onMessage(Session session, String message){
        log.info("[onOpen][session({}) 接收到一条消息({})]", session, message);
        try {
            session.getBasicRemote().sendText("hello: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        log.info("[onClose][session({}) 连接关闭。关闭的原因是({})]", session, closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        log.info("[onClose][session({}) 发生异常({})]", session, throwable);
    }
}