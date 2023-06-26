package com.qin.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Log4j2
@Component
public class TestWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      log.info("有人成功连接", session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        log.info("有人成功连接", session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("有人发送消息", session);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("有人发送消息", session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("有人关闭", session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
}
