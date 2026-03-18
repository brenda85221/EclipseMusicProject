package team5.forum.model;


public class NotificationMessage {
    private Integer notificationId;
    private String message;
    private Integer articleId;

    public NotificationMessage() {
    }

    public NotificationMessage(Integer notificationId, String message, Integer articleId) {
        this.notificationId = notificationId;
        this.message = message;
        this.articleId = articleId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}
