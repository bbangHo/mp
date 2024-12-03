package kr.pknu.SonYoungHo201911958.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.pknu.SonYoungHo201911958.config.ApplicationCofinguration;
import kr.pknu.SonYoungHo201911958.dto.WeatherResponse;

public class WeatherApi {
    private static final String BASE_URL = ApplicationCofinguration.URL;

    public interface WeatherCallback {
        void onSuccess(WeatherResponse.Result result);

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
                    Log.d("WeatherApi", "Response: " + response.toString());
                    if (jsonResponse.getBoolean("isSuccess")) {
                        JSONObject result = jsonResponse.getJSONObject("result");
                        WeatherResponse.Result res = new WeatherResponse.Result(
                                result.getString("city"),
                                result.getString("street"),
                                result.getString("currentSkyType"),
                                result.getInt("currentTmp")
                        );

                        JSONArray weatherPerHourList = result.getJSONArray("weatherPerHourList");
                        List<WeatherResponse.HourlyWeatherData> weatherData = new ArrayList<>();

                        for (int i = 0; i < weatherPerHourList.length(); i++) {
                            JSONObject data = weatherPerHourList.getJSONObject(i);
                            WeatherResponse.HourlyWeatherData item = new WeatherResponse.HourlyWeatherData(
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

                        res.setHourlyWeatherData(weatherData);
                        callback.onSuccess(res);
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

