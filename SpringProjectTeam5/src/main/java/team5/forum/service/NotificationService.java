package team5.forum.service;


import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team5.forum.handler.WebSocketServer;
import team5.forum.model.Notification;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 存儲用戶連線的 WebSocket session
    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    // 註冊用戶連線
    public void registerUser(String acct, Session session) {
        sessions.put(acct, session);
    }

    // 移除用戶連線
    public void unregisterUser(String acct) {
        sessions.remove(acct);
    }

    // 發送未讀通知
//    public void sendUndeliveredNotifications(String acct) {
//        sendMessage(acct, "這是你的未讀通知！");
//    }

    // 推送消息
    public void sendMessage(String acct, String message) {
        Session session = sessions.get(acct);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("無法找到用戶的 WebSocket 連接: " + acct);
        }
    }

    // 發送通知
    public void notifyUser(String acct, String message, Integer articleId) {
        if (WebSocketServer.isUserConnected(acct)) {
            Notification notification = new Notification();
            notification.setArticleId(articleId);
            notification.setAcct(acct);
            notification.setNotificationContent(message);
            notification.setIsDelivered(1);
            Notification notificationSaved = notificationRepository.save(notification);
            // 如果用戶在線，直接通過 WebSocket 發送消息
            WebSocketServer.sendMessage(acct, message, notificationSaved.getNotificationId(), notificationSaved.getArticleId());
        } else {
            // 如果用戶不在線，儲存到資料庫
            Notification notification = new Notification();
            notification.setAcct(acct);
            notification.setArticleId(articleId);
            notification.setNotificationContent(message);
            notification.setIsDelivered(0);
            notificationRepository.save(notification);
        }
    }

    // 發送未讀消息
    public void sendUndeliveredNotifications(String acct) {
        List<Notification> undeliveredNotifications = notificationRepository.findByAcctAndIsDelivered(acct, 0);
        for (Notification notification : undeliveredNotifications) {
            WebSocketServer.sendMessage(acct, notification.getNotificationContent(), notification.getNotificationId(), notification.getArticleId());
            notification.setIsDelivered(1);
            notificationRepository.save(notification);
        }
    }

    public List<Notification> getAllByAcct(String acct) {
        return notificationRepository.findAllByAcct(acct, Sort.by(Sort.Order.desc("createdAt")));
    }

    public void updateIsOpened(Integer notificationId, Integer isOpened) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isPresent()) {
            // 取得通知物件
            Notification notification = optionalNotification.get();
            System.out.println(notification.toString());
            // 更新 isOpened 欄位
            notification.setIsOpened(isOpened);
            // 保存更新後的物件
            Notification save = notificationRepository.save(notification);
            System.out.println("save = " + save.toString());
        } else {
            throw new RuntimeException("Notification not found with id: " + notificationId);
        }
    }

    public void updateIsRead(Integer notificationId, Integer isRead) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isPresent()) {
            // 取得通知物件
            Notification notification = optionalNotification.get();
            System.out.println(notification.toString());
            // 更新 isRead 欄位
            notification.setIsRead(isRead);
            // 保存更新後的物件
            Notification save = notificationRepository.save(notification);
            System.out.println("save = " + save.toString());
        } else {
            throw new RuntimeException("Notification not found with id: " + notificationId);
        }
    }
}
