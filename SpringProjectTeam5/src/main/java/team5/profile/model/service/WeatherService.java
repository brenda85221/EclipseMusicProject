package team5.profile.model.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherService {
    public String getWeatherDataByCounty(String county) throws Exception {
    	@Value("${weather.api.key}")
    	private String apiKey;
        String apiUrl = "https://opendata.cwa.gov.tw/api/v1/rest/datastore/O-A0003-001?Authorization="
                + apiKey + "&locationName=" + county;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new Exception("Failed to fetch data, HTTP status: " + status);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // 解析 API 回傳的 JSON，並篩選出需要的資料
        JSONObject json = new JSONObject(response.toString());
        JSONObject stationData = json.getJSONObject("records").getJSONArray("Station").getJSONObject(0);
        JSONObject weatherElement = stationData.getJSONObject("WeatherElement");

        JSONObject result = new JSONObject();
        result.put("Weather", weatherElement.getString("Weather"));
        result.put("AirTemperature", weatherElement.getDouble("AirTemperature"));

        return result.toString();
    }
}
