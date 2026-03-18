package team5.forum.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team5.forum.model.NotificationMessage;
import team5.forum.service.NotificationService;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{acct}")
public class WebSocketServer {

    // 儲存連接中的用戶 (key: userId, value: Session)
    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    private static NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        WebSocketServer.notificationService = notificationService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("acct") String acct) {
        sessions.put(acct, session);
        System.out.println("用戶連接: " + acct);
        notificationService.registerUser(acct, session);
        // 查詢未讀消息並推送
        notificationService.sendUndeliveredNotifications(acct);
    }

    @OnClose
    public void onClose(@PathParam("acct") String acct) {
        sessions.remove(acct);
        System.out.println("用戶斷開: " + acct);
        notificationService.unregisterUser(acct);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket 發生錯誤: " + error.getMessage());
    }

    // 發送消息給指定用戶
    public static void sendMessage(String acct, String message, Integer notificationId, Integer articleId) {
        Session session = sessions.get(acct);
        if (session != null && session.isOpen()) {
            try {
                // 使用 JSON 包裝消息
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(new NotificationMessage(notificationId, message, articleId));

                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 檢查用戶是否已連接
    public static boolean isUserConnected(String acct) {
        return sessions.containsKey(acct);
    }
}
