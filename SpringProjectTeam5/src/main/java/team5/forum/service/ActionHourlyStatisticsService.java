package team5.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.model.ActionHourlyStatistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ActionHourlyStatisticsService {

    @Autowired
    private ActionHourlyStatisticsRepository actionStatRepo;

    public List<ActionHourlyStatistics> getByHour(){
        System.out.println("feffefefefe = " + LocalDateTime.now().getHour());
        return actionStatRepo.findByHour(10);
    }

    public List<ActionHourlyStatistics> getByDate(String dateString, Integer startHour, Integer endHour, String actionType) {
        // 定義日期格式，假設傳入的日期字符串格式為 "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 將 String 轉換為 LocalDate
        LocalDate date = LocalDate.parse(dateString, formatter);

        // 將 LocalDate 轉換為 LocalDateTime（當天的開始時間 00:00:00）
        LocalDateTime startOfDay = date.atStartOfDay();

        // 計算結束時間（當天的 23:59:59）
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        return actionStatRepo.findByDateRangeAndHourRange(startOfDay, endOfDay, startHour, endHour, actionType);
    }

}
