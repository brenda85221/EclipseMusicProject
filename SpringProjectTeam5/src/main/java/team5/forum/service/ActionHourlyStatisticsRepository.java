package team5.forum.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team5.forum.model.ActionHourlyStatistics;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionHourlyStatisticsRepository extends JpaRepository<ActionHourlyStatistics, Integer> {

    // 查詢某一行為類型的統計數據
    List<ActionHourlyStatistics> findByActionTypeAndHour(String activityType, int hour);

    // 查詢所有行為類型的統計數據
    List<ActionHourlyStatistics> findByHour(int hour);

    // 根據日期範圍查詢每小時活動數據
    @Query("""
        SELECT a FROM ActionHourlyStatistics a 
        WHERE a.actionDate BETWEEN :startOfDay AND :endOfDay
        AND a.hour BETWEEN :startHour AND :endHour
        AND a.actionType = :actionType
       """)
    List<ActionHourlyStatistics> findByDateRangeAndHourRange(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("startHour") int startHour,
            @Param("endHour") int endHour,
            @Param("actionType") String actionType);
}
