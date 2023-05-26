package com.example.chatting;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@ServerEndpoint("/socket/chatt")
public class WebSocketChat {
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
    private static Logger logger = LoggerFactory.getLogger(WebSocketChat.class);

    //클라이언트가 접속할 때마다 동작 로직
    @OnOpen
    public void onOpen(Session session) {
        logger.info("open session : {}, clients={}", session.toString(), clients);
        Map<String, List<String>> res = session.getRequestParameterMap();
        logger.info("res={}", res);

        if(!clients.contains(session)) {
            clients.add(session);
            logger.info("session open : {}", session);
        }else{
            logger.info("이미 연결된 session");
        }
    }

    //메세지 수신 시 동작 로직
    @OnMessage
    public void onMessage(String message) throws IOException {
        logger.info("receive message : {}", message);

        for (Session s : clients) {
            logger.info("who are you : {}", s);
            logger.info("who are you : {}", s.getId());
            logger.info("send data : {}", message);
            s.getBasicRemote().sendText(message);
        }
    }

    //클라이언트가 접속을 종료할 시 동작 로직
    @OnClose
    public void onClose(Session session) {
        logger.info("session close : {}", session);
        clients.remove(session);
    }

}
