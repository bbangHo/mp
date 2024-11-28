package kr.pknu.SonYoungHo201911958;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.List;

public class HourlyForecastFragment extends Fragment {
    private LinearLayout container;
    private String accessToken;
    private boolean showText;

    public HourlyForecastFragment(String accessToken, boolean showText) {
        this.accessToken = accessToken;
        this.showText = showText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hourly_forecast, container, false);
        this.container = rootView.findViewById(R.id.hourly_forecast_container);

        fetchWeatherData();

        return rootView;
    }

    private void fetchWeatherData() {
        WeatherApi.fetchHourlyWeather(accessToken, "703", new WeatherApi.WeatherCallback() {
            @Override
            public void onSuccess(List<HourlyWeatherData> weatherData) {
                populateHourlyForecast(weatherData);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
    }

    private void populateHourlyForecast(List<HourlyWeatherData> hourlyData) {
        container.removeAllViews();

        for (HourlyWeatherData item : hourlyData) {
            View cardView = createWeatherCard(getContext(), item);
            container.addView(cardView);
        }
    }

    private View createWeatherCard(Context context, HourlyWeatherData item) {
        CardView card = new CardView(context);
        card.setRadius(10);
        card.setCardElevation(4);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout cardContent = new LinearLayout(context);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(16, 16, 16, 16);
        card.addView(cardContent);

        // 시간
        TextView timeView = new TextView(context);
        timeView.setText(formatHour(item.getHour()));
        timeView.setTextSize(14);
        timeView.setTextColor(context.getResources().getColor(android.R.color.white));
        timeView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cardContent.addView(timeView);

        // 날씨 아이콘
        ImageView weatherIcon = new ImageView(context);
        weatherIcon.setImageResource(getWeatherIcon(item.getSkyType(), item.getRain()));
        weatherIcon.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
        cardContent.addView(weatherIcon);

        // 강수량
        TextView rainView = new TextView(context);
        rainView.setText(showText ? item.getRainText() : item.getRain() + "mm");
        rainView.setTextSize(14);
        rainView.setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
        rainView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cardContent.addView(rainView);

        // 온도
        TextView tempView = new TextView(context);
        tempView.setText(showText ? item.getTmpText() : item.getTmp() + "°C");
        tempView.setTextSize(14);
        tempView.setTextColor(context.getResources().getColor(android.R.color.white));
        tempView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cardContent.addView(tempView);

        return card;
    }

    private String formatHour(String isoString) {
        // ISO 시간 문자열을 포맷팅
        return isoString.substring(11, 13) + "시";
    }

    private int getWeatherIcon(String skyType, int rain) {
        if (rain > 0) {
            return R.drawable.icon_rain;
        }
        switch (skyType) {
            case "CLEAR":
                return R.drawable.icon_clear;
            case "PARTLYCLOUDY":
                return R.drawable.icon_partlycloudy;
            case "CLOUDY":
                return R.drawable.icon_cloudy;
            default:
                return R.drawable.icon_default;
        }
    }
}

