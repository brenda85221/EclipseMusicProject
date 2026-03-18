package team5.forum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationId")
    private Integer notificationId;

    @Column(name = "acct")
    private String acct;

    @Column(name = "articleId")
    private Integer articleId;

    @Column(name = "notificationContent")
    private String notificationContent;

    @Column(name = "isDelivered")
    private Integer isDelivered;

    @Column(name = "isRead", insertable = false)
    private Integer isRead;

    @Column(name = "isOpened", insertable = false)
    private Integer isOpened;

    @Column(name = "createdAt", updatable = false, insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Date createdAt;

    public Notification() {
    }

    public Notification(Integer notificationId, String acct, Integer articleId, String notificationContent, Integer isDelivered, Integer isRead, Integer isOpened, Date createdAt) {
        this.notificationId = notificationId;
        this.acct = acct;
        this.articleId = articleId;
        this.notificationContent = notificationContent;
        this.isDelivered = isDelivered;
        this.isRead = isRead;
        this.isOpened = isOpened;
        this.createdAt = createdAt;
    }

    public Notification(String acct, String notificationContent, Integer isRead) {
        this.acct = acct;
        this.notificationContent = notificationContent;
        this.isRead = isRead;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public Integer getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(Integer isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Integer isOpened) {
        this.isOpened = isOpened;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", acct='" + acct + '\'' +
                ", articleId=" + articleId +
                ", notificationContent='" + notificationContent + '\'' +
                ", isDelivered=" + isDelivered +
                ", isRead=" + isRead +
                ", isOpened=" + isOpened +
                ", createdAt=" + createdAt +
                '}';
    }
}
