package kr.pknu.SonYoungHo201911958;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherApi {
    private static final String BASE_URL = "http://13.125.128.147:8080";

    public interface WeatherCallback {
        void onSuccess(List<HourlyWeatherData> weatherData);
        void onError(String errorMessage);
    }

    public static void fetchHourlyWeather(String accessToken, String locationId, WeatherCallback callback) {
        new Thread(() -> {
            try {
                String urlString = locationId != null
                        ? BASE_URL + "/api/v1/main/weather?locationId=" + locationId
                        : BASE_URL + "/api/v1/main/weather";

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.getBoolean("isSuccess")) {
                        JSONObject result = jsonResponse.getJSONObject("result");
                        JSONArray weatherPerHourList = result.getJSONArray("weatherPerHourList");
                        List<HourlyWeatherData> weatherData = new ArrayList<>();

                        for (int i = 0; i < weatherPerHourList.length(); i++) {
                            JSONObject data = weatherPerHourList.getJSONObject(i);
                            HourlyWeatherData item = new HourlyWeatherData(
                                    data.getString("hour"),
                                    data.getString("skyType"),
                                    data.getString("rainAdverb"),
                                    data.getString("rainText"),
                                    data.getInt("rain"),
                                    data.getString("tmpAdverb"),
                                    data.getString("tmpText"),
                                    data.getInt("tmp")
                            );
                            weatherData.add(item);
                        }

                        callback.onSuccess(weatherData);
                    } else {
                        callback.onError(jsonResponse.getString("message"));
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("WeatherApi", "Error fetching weather data", e);
                callback.onError(e.getMessage());
            }
        }).start();
    }
}

