package kr.pknu.SonYoungHo201911958;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button toggleViewButton;
    private FrameLayout weatherInfoSlider;
    private TextView currentLocation;
    private LinearLayout posts;
    private LinearLayout hourlyForecastContainer;
    private FrameLayout airQuality;
    private boolean showText = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        toggleViewButton = findViewById(R.id.toggleViewButton);
        weatherInfoSlider = findViewById(R.id.weatherInfoSlider);
//        currentLocation = findViewById(R.id.currentLocation);
//        posts = findViewById(R.id.posts);
        hourlyForecastContainer = findViewById(R.id.hourlyForecast); // ID 매핑
        airQuality = findViewById(R.id.airQuality);

        toggleViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showText = !showText;

                if (showText) {
                    toggleViewButton.setText("수치");
                } else {
                    toggleViewButton.setText("텍스트");
                }

                loadHourlyForecast();
            }
        });

        // Load initial data
        loadWeatherInfoSlider();
//        loadCurrentLocation();
        loadPosts();
        loadHourlyForecast();
        loadAirQuality();
    }

    private void toggleView() {
        // Logic to toggle between different views
    }

    private void loadWeatherInfoSlider() {
        // Load and display weather information
    }

//    private void loadCurrentLocation() {
//        // Fetch and display current location
//        currentLocation.setText("임시 지역");
//    }

    private void loadPosts() {
        // Dynamically add posts to the posts layout
    }

//    private void loadHourlyForecast() {
//        // Fetch hourly forecast data and display it
//        String accessToken = "yourAccessToken"; // Replace with actual token
//        String locationId = "703";   // Replace with actual location ID
//
//        WeatherApi.fetchHourlyWeather(accessToken, locationId, new WeatherApi.WeatherCallback() {
//            @Override
//            public void onSuccess(List<HourlyWeatherData> weatherData) {
//                runOnUiThread(() -> populateHourlyForecast(weatherData));
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.e("HomeActivity", "Error fetching hourly forecast: " + errorMessage);
//            }
//        });
//    }

    private void populateHourlyForecast(List<HourlyWeatherData> weatherData) {
        hourlyForecastContainer.removeAllViews();

        for (HourlyWeatherData data : weatherData) {
            View cardView = createWeatherCard(data);
            hourlyForecastContainer.addView(cardView);
        }
    }

    // 동적으로 생성되는 컴포넌트 공통 컨테이너 스타일 적용 메서드
    private Drawable createCommonBackgroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#33FFFFFF"));  // 20% 흰색
        drawable.setCornerRadius(20);  // 모서리 반경 설정

        return drawable;
    }

    // 동적으로 생성되는 컴포넌트 공통 텍스트 스타일 적용 메서드
    private TextView addCommonTextStyle(TextView textView, boolean isBoldStyle) {
        textView.setTextColor(Color.WHITE);  // 텍스트 색상
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);  // 텍스트 크기 (16sp)

        if (isBoldStyle) {
            textView.setTypeface(null, Typeface.BOLD);  // 볼드 텍스트
        }

        return textView;
    }

    @SuppressLint("SetTextI18n")
    private View createWeatherCard(HourlyWeatherData data) {
        CardView card = new CardView(this);

        // 둥근 모서리
        card.setRadius(10);
        card.setCardElevation(4);
        card.setBackground(createCommonBackgroundDrawable());       // 공통 컨테이너 스타일

        // 카드 간격 설정
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // w
                LinearLayout.LayoutParams.WRAP_CONTENT  // h
        );

        cardParams.setMargins(24, 16, 24, 16);

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(16, 16, 16, 16);
//        cardContent.setBackground(createCommonBackgroundDrawable());    // 공통 컨테이너 스타일
        cardContent.setLayoutParams(cardParams);
        card.addView(cardContent);

        // 시간 표시
        TextView timeView = new TextView(this);
        timeView.setText(formatHour(data.getHour()));
        timeView.setTextSize(14);
        timeView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cardContent.addView(timeView);

        // 날씨 아이콘 추가
        ImageView weatherIcon = new ImageView(this);
        weatherIcon.setImageResource(getWeatherIcon(data.getSkyType(), data.getRain())); // 아이콘 설정
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(100, 100); // 아이콘 크기
        iconParams.setMargins(0, 16, 0, 16);
        weatherIcon.setLayoutParams(iconParams);
        cardContent.addView(weatherIcon);

        // 강수량 표시
        TextView rainView = new TextView(this);
        rainView.setText(showText ? data.getRainText() : data.getRain() + "mm");
        rainView.setTextSize(14);
        rainView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cardContent.addView(rainView);

        // 온도 표시
        TextView tempView = new TextView(this);
        tempView.setText(showText ? data.getTmpText() : data.getTmp() + "°C");
        tempView.setTextSize(14);
        tempView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cardContent.addView(tempView);

        return card;
    }

    private String formatHour(String isoString) {
        return isoString.substring(11, 13) + "시"; // ISO 8601 시간 문자열에서 시간만 추출
    }

    private void loadAirQuality() {
        // Show air quality index
    }

    private int getWeatherIcon(String skyType, int rain) {
        switch (skyType) {
            case "CLEAR":
                return R.drawable.icon_clear; // 맑음 아이콘
            case "PARTLYCLOUDY":
                return R.drawable.icon_partlycloudy; // 구름 조금
            case "CLOUDY":
                return R.drawable.icon_cloudy; // 흐림
            case "RAIN":
                return R.drawable.icon_rain;    // 비옴
            default:
                return R.drawable.icon_default; // 기본 아이콘
        }
    }


    // 더미 데이터를 생성하는 메서드
    private List<HourlyWeatherData> createDummyHourlyData() {
        List<HourlyWeatherData> dummyData = new ArrayList<>();
        dummyData.add(new HourlyWeatherData(
                "2024-08-19T07:00:00", "CLOUDY", "매우", "강함", 80, "", "추움", 6));
        dummyData.add(new HourlyWeatherData(
                "2024-08-19T08:00:00", "PARTLYCLOUDY", "매우", "강함", 90, "", "선선", 19));
        dummyData.add(new HourlyWeatherData(
                "2024-08-19T09:00:00", "CLEAR", "매우", "강함", 100, "", "추움", 6));
        dummyData.add(new HourlyWeatherData(
                "2024-08-19T10:00:00", "RAIN", "", "보통", 10, "", "더움", 34));
        dummyData.add(new HourlyWeatherData(
                "2024-08-19T11:00:00", "CLOUDY", "", "보통", 10, "", "더움", 34));
        dummyData.add(new HourlyWeatherData(
                "2024-08-19T12:00:00", "CLOUDY", "", "보통", 10, "", "더움", 34));
        dummyData.add(new HourlyWeatherData(
                "2024-08-19T13:00:00", "CLOUDY", "", "보통", 10, "", "더움", 34));

        return dummyData;
    }

    // 더미 데이터를 로드하는 메서드
    private void loadHourlyForecast() {
        List<HourlyWeatherData> dummyData = createDummyHourlyData(); // 더미 데이터 생성
        populateHourlyForecast(dummyData); // 더미 데이터를 UI에 반영
    }
}
