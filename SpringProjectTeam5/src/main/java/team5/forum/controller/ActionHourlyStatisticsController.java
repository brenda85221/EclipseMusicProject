package team5.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.ActionHourlyStatistics;
import team5.forum.service.ActionHourlyStatisticsService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/forum/article/analyze")
public class ActionHourlyStatisticsController {

    @Autowired
    private ActionHourlyStatisticsService actionStatService;

    @GetMapping("/hourly")
    public List<ActionHourlyStatistics> getHourlyStatistics() {
        // 查詢過去幾小時的統計數據（例如：返回過去 24 小時的統計數據）
        return actionStatService.getByHour();
    }

    @GetMapping("/date")
    public List<ActionHourlyStatistics> getByDateStatistics(
            @RequestParam("selectedDate") String selectedDate,
            @RequestParam("startHour") Integer startHour,
            @RequestParam("endHour") Integer endHour,
            @RequestParam("actionType") String actionType) {
        return actionStatService.getByDate(selectedDate, startHour, endHour, actionType);
    }
}
