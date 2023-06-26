package com.qin.websocket.chat;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryChatRoom {
    private final ConcurrentHashMap<Long, WebSocketSession> room = new ConcurrentHashMap<Long, WebSocketSession>();

//   public void sendMessage();
}
