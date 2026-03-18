package team5.forum.controller;

import jakarta.persistence.EntityManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.service.ActionHourlyStatisticsRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class ActionStatisticsScheduler {
    private final ActionHourlyStatisticsRepository statisticsRepository;
    private final EntityManager entityManager;

    public ActionStatisticsScheduler(ActionHourlyStatisticsRepository statisticsRepository, EntityManager entityManager) {
        this.statisticsRepository = statisticsRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * ?") // 每小時執行一次
    public void updateHourlyStatistics() {
        LocalDate today = LocalDate.now();

        // 定義需要統計的行為類型
        List<String> actionTypes = List.of("click", "like", "comment", "report");

        for (String actionType : actionTypes) {
            String query = generateInsertQuery(actionType);

            // 使用 Native Query 執行插入操作
            entityManager.createNativeQuery(query)
                    .setParameter("today", today)
                    .setParameter("actionType", actionType)
                    .executeUpdate();
        }

        System.out.println("Hourly statistics updated successfully.");
    }

    private String generateInsertQuery(String actionType) {
        String dateField;
        String tableName;

        // 根據 actionType 動態生成 SQL
        switch (actionType) {
            case "click":
                dateField = "clickArticleDate";
                tableName = "articleViewCount";
                break;
            case "like":
                dateField = "likeArticleDate";
                tableName = "articleLikeCount";
                break;
            case "comment":
                dateField = "commentPostTime";
                tableName = "comment";
                break;
            case "report":
                dateField = "reportTime";
                tableName = "report";
                break;
            default:
                throw new IllegalArgumentException("Unsupported action type: " + actionType);
        }

        return """
        MERGE INTO actionHourlyStatistics AS target
        USING (
            SELECT :actionType AS actionType, CAST(:today AS DATE) AS actionDate,
                   DATEPART(HOUR, %s) AS hour, COUNT(*) AS actionCount, GETDATE() AS createdAt
            FROM %s
            WHERE %s >= CAST(:today AS DATETIME)
            GROUP BY DATEPART(HOUR, %s)
        ) AS source
        ON target.actionType = source.actionType 
           AND target.actionDate = source.actionDate 
           AND target.hour = source.hour
        WHEN MATCHED THEN
            UPDATE SET target.actionCount = source.actionCount, target.createdAt = source.createdAt
        WHEN NOT MATCHED THEN
            INSERT (actionType, actionDate, hour, actionCount, createdAt)
            VALUES (source.actionType, source.actionDate, source.hour, source.actionCount, source.createdAt);
        """.formatted(dateField, tableName, dateField, dateField);
    }

}
