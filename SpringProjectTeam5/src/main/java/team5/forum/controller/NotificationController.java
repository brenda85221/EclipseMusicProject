package team5.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.Notification;
import team5.forum.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/forum/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notify")
    public String notifyUser(@RequestParam String acct, @RequestParam String message, @RequestParam Integer articleId) {
        notificationService.notifyUser(acct, message, articleId);
        return "通知已發送給: " + acct;
    }

    @GetMapping("/get/{acct}")
    public List<Notification> queryNotificationByAcct(@PathVariable("acct") String acct) {
        return notificationService.getAllByAcct(acct);
    }

    @PutMapping("/update/isOpened")
    public String updateNotificationIsOpened(@RequestParam Integer notificationId, @RequestParam Integer isOpened) {
        notificationService.updateIsOpened(notificationId, isOpened);
        return "isOpened狀態更新成功";
    }

    @PutMapping("/update/isRead")
    public String updateNotificationIsRead(@RequestParam Integer notificationId, @RequestParam Integer isRead) {
        notificationService.updateIsRead(notificationId, isRead);
        return "isRead狀態更新成功";
    }
}
