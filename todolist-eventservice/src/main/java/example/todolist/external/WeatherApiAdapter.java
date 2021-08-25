package example.todolist.external;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WeatherApiAdapter {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(WeatherApiAdapter.class);
    private static final String WEATHER_API_KEY = System.getenv("WEATHER_API_KEY");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    public WeatherApiResult getWeatherData(String location, LocalDateTime eventDate) {
        if (StringUtils.isEmpty(WEATHER_API_KEY)) {
            return null;
        }

        if (LocalDateTime.now().plusDays(2).isBefore(eventDate)) {
            return null;
        }

        LOG.info("Attempting to retrieve weather data for location: {}, eventDate: {}",
                location, eventDate);
        final String apiUrl = "http://api.weatherapi.com/v1/forecast.json?q="
                + URLEncoder.encode(location)
                + "&key=" + URLEncoder.encode(WEATHER_API_KEY) +"&days=3";
        GetRequest apiRequest = Unirest.get(apiUrl);
        try {
            HttpResponse<JsonNode> jsonResponse = apiRequest.asJson();
            JSONObject body = jsonResponse.getBody().getObject();

            WeatherApiResult result = new WeatherApiResult();
            if (body.has("location")) {
                JSONObject locationObject = body.getJSONObject("location");
                result.setLocationName(locationObject.getString("name")
                        + ", " + locationObject.getString("region")
                        + ", " + locationObject.getString("country"));
            }
            if (body.has("forecast")) {
                String eventDateStr = eventDate.format(DATE_TIME_FORMATTER);
                JSONObject forecastObject = body.getJSONObject("forecast");
                JSONArray forecastDays = forecastObject.getJSONArray("forecastday");
                for (int i = 0; i < forecastDays.length(); i++) {
                    JSONObject dayForecast = forecastDays.getJSONObject(i);
                    if (dayForecast.has("date") && eventDateStr.equals(dayForecast.getString("date"))) {
                        JSONObject forecastData = dayForecast.getJSONObject("day");
                        result.setTemperature(Double.toString(forecastData.getDouble("maxtemp_f")));
                        result.setWindSpeed(Double.toString(forecastData.getDouble("maxwind_mph")));
                        result.setConditions("CLEAR");
                        if (forecastData.has("daily_will_it_snow") && forecastData.getInt("daily_will_it_snow") == 1) {
                            result.setConditions("SNOWY");
                        } else if (forecastData.has("daily_will_it_rain") && forecastData.getInt("daily_will_it_rain") == 1) {
                            result.setConditions("RAINY");
                        }
                    }
                }
                LOG.info("Successfully to retrieved weather data for location: {}, eventDate: {}",
                        location, eventDate);
                return result;
            }
            LOG.info("Unable to retrieve weather data for location: {}, eventDate: {}",
                    location, eventDate);
        } catch (UnirestException e) {
            LOG.error("Unable to retrieve weather data for location: {}, eventDate: {}",
                    location, eventDate, e);
        }
        return null;
    }

    @Data
    public static class WeatherApiResult {
        private String locationName;
        private String conditions;
        private String temperature;
        private String windSpeed;
    }
}
