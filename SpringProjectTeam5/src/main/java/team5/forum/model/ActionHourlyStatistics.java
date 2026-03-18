package team5.forum.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
//@Table(name = "actionHourlyStatistics")
@Table(name = "actionHourlyStatistics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"actionDate", "hour", "actionType"})
})
public class ActionHourlyStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actionStatId")
    private Integer actionStatId;

    @Column(name = "actionType")
    private String actionType;  // 行為類型

    @Column(name = "actionDate")
    private Date actionDate;

    @Column(name = "hour")
    private int hour;             // 小時 (0-23)

    @Column(name = "actionCount")
    private int actionCount;    // 該小時的行為數量

    @Column(name = "createdAt")
    private Date createdAt;  // 創建時間

    public ActionHourlyStatistics() {
    }

    public ActionHourlyStatistics(Integer actionStatId, String actionType, Date actionDate, int hour, int actionCount, Date createdAt) {
        this.actionStatId = actionStatId;
        this.actionType = actionType;
        this.actionDate = actionDate;
        this.hour = hour;
        this.actionCount = actionCount;
        this.createdAt = createdAt;
    }

    public Integer getActionStatId() {
        return actionStatId;
    }

    public void setActionStatId(Integer actionStatId) {
        this.actionStatId = actionStatId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getActionCount() {
        return actionCount;
    }

    public void setActionCount(int actionCount) {
        this.actionCount = actionCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
