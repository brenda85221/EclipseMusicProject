package team5.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team5.profile.model.service.WeatherService;

@RestController
@RequestMapping("/profile/public")
public class WeatherController {

    @GetMapping("/weather")
    public String getWeatherData(@RequestParam String county) {
        WeatherService weatherService = new WeatherService();
        try {
            String data = weatherService.getWeatherDataByCounty(county);
            return data;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
