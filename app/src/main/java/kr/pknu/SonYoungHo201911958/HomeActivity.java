package kr.pknu.SonYoungHo201911958;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import kr.pknu.SonYoungHo201911958.api.PostApi;
import kr.pknu.SonYoungHo201911958.api.WeatherApi;
import kr.pknu.SonYoungHo201911958.config.ApplicationCofinguration;
import kr.pknu.SonYoungHo201911958.dto.PostResponse;
import kr.pknu.SonYoungHo201911958.dto.WeatherResponse;

public class HomeActivity extends AppCompatActivity {
    private WeatherResponse.Result cachedWeatherData;

    private Button toggleViewButton;
    private ImageView likeImage;
    private LinearLayout postContainer;
    private LinearLayout postOuterContainer;
    private LinearLayout hourlyForecastContainer;
    private FrameLayout airQuality;
    private boolean showText = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        toggleViewButton = findViewById(R.id.toggleViewButton);
        likeImage = findViewById(R.id.likeImage);
        postOuterContainer = findViewById(R.id.postOuterContainer);
        postContainer = findViewById(R.id.postContainer);
        hourlyForecastContainer = findViewById(R.id.hourlyForecast);
        airQuality = findViewById(R.id.airQuality);

        // 수치 <-> 텍스트
        toggleViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showText = !showText;

                if (showText) {
                    toggleViewButton.setText("수치");
                } else {
                    toggleViewButton.setText("텍스트");
                }

                // API 재호출 없이 cachedWeatherData를 사용해 UI 업데이트
                if (cachedWeatherData != null) {
                    updateWeatherUI(cachedWeatherData);
                }
            }
        });

        postContainer.removeAllViews();
        loadPosts(0, 10, "WEATHER", 703);
        loadWeatherData();
        loadAirQuality();
    }

    private void loadPosts(int lastPostId, int size, String postType, int locationId) {
        // API 호출하여 게시글을 가져옴
        PostApi.getPosts(lastPostId, size, postType, locationId, new PostApi.PostCallback() {
            @Override
            public void onSuccess(List<PostResponse.Post> posts) {
                // 데이터를 성공적으로 받았을 때, UI에 게시글을 동적으로 추가
                displayPosts(posts);
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this, "게시글을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void displayPosts(List<PostResponse.Post> posts) {
        final Activity activity = this;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // posts 리스트를 반복하며 각각의 post에 대해 view 생성
                for (PostResponse.Post post : posts) {

                    // 각 게시글에 대한 LinearLayout을 새로 만들어서 배경을 설정합니다.
                    LinearLayout postContainer = new LinearLayout(activity);
                    postContainer.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams postContainerLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    postContainerLayoutParams.setMargins(0, 0, 0, convertDpToPx(8));
                    postContainer.setLayoutParams(postContainerLayoutParams);
                    postContainer.setBackgroundResource(R.drawable.common_container_style);

                    // 작성자 정보 및 프로필 이미지를 동적으로 추가
                    LinearLayout postInfoContainer = new LinearLayout(activity);
                    postInfoContainer.setOrientation(LinearLayout.HORIZONTAL);
                    postInfoContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            convertDpToPx(360), LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    postInfoContainer.setPadding(convertDpToPx(8), convertDpToPx(4), convertDpToPx(8), convertDpToPx(4));

                    // 게시글 간 간격 추가
                    LinearLayout.LayoutParams postInfoContainerLayoutParams = (LinearLayout.LayoutParams) postInfoContainer.getLayoutParams();
                    postInfoContainerLayoutParams.setMargins(0, 0, 0, convertDpToPx(8));  // 아래에 여백 추가
                    postInfoContainer.setLayoutParams(postInfoContainerLayoutParams);

                    // 프로필 이미지
                    ImageView userProfile = new ImageView(activity);
                    LinearLayout.LayoutParams userProfileLayoutParams = new LinearLayout.LayoutParams(
                            convertDpToPx(30), convertDpToPx(30));
                    userProfileLayoutParams.setMargins(0, convertDpToPx(10), convertDpToPx(4), 0);
                    userProfile.setLayoutParams(userProfileLayoutParams);
                    userProfile.setImageResource(R.drawable.profile);
                    userProfile.setBackgroundResource(R.drawable.common_conner_style);  // 배경 추가
                    userProfile.setClipToOutline(true); // 이미지를 둥글게 자르기
                    postInfoContainer.addView(userProfile);

                    // 작성자 이름과 시간
                    LinearLayout userInfoContainer = new LinearLayout(activity);
                    userInfoContainer.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams userInfoContainerLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    userInfoContainerLayoutParams.setMargins(0, convertDpToPx(8), 0, 0);
                    userInfoContainer.setLayoutParams(userInfoContainerLayoutParams);

                    TextView userName = new TextView(activity);
                    userName.setText(post.getMemberInfo().getMemberName());
                    userName.setTextSize(14);
                    userName.setTextAppearance(activity, R.style.CommonTextStyle);
                    userInfoContainer.addView(userName);

                    TextView createdAt = new TextView(activity);
                    createdAt.setText(post.getPostInfo().getCreatedAt()); // "1일 전"과 같은 시간 정보
                    createdAt.setTextColor(getResources().getColor(R.color.light_gray));  // 시간 텍스트 색상
                    createdAt.setTextSize(12);
                    userInfoContainer.addView(createdAt);
                    postInfoContainer.addView(userInfoContainer);

                    // 사용자 타입 이미지
                    ImageView userType = new ImageView(activity);
                    LinearLayout.LayoutParams userTypeParam = new LinearLayout.LayoutParams(convertDpToPx(20), convertDpToPx(20));
                    userTypeParam.setMargins(0, convertDpToPx(8), 0, 0);
                    userType.setLayoutParams(userTypeParam);
                    if (post.getMemberInfo().getSensitivity().equals("NONE")) {
                        userType.setImageResource(R.drawable.icon_partlycloudy);  // 사용자 타입에 맞는 이미지
                    } else if (post.getMemberInfo().getSensitivity().equals("COLD")) {
                        userType.setImageResource(R.drawable.icon_snow);  // 사용자 타입에 맞는 이미지
                    } else {
                        userType.setImageResource(R.drawable.icon_clear);  // 사용자 타입에 맞는 이미지
                    }
                    postInfoContainer.addView(userType);

                    // 좋아요 이미지와 좋아요 수
                    LinearLayout postLikeContainer = new LinearLayout(activity);
                    postLikeContainer.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams postLikeContainerLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    postLikeContainerLayoutParams.setMargins(convertDpToPx(240), convertDpToPx(8), 0, 0);
                    postLikeContainer.setLayoutParams(postLikeContainerLayoutParams);

                    ImageView likeImage = new ImageView(activity);
                    likeImage.setLayoutParams(new LinearLayout.LayoutParams(convertDpToPx(20), convertDpToPx(20)));
                    likeImage.setImageResource(post.getPostInfo().isLikeClickable() ?
                            R.drawable.icon_heart0 : R.drawable.icon_heart2);  // 좋아요 클릭 여부에 따른 이미지 변경
                    postLikeContainer.addView(likeImage);

                    TextView likeCount = new TextView(activity);
                    likeCount.setText(String.valueOf(post.getPostInfo().getLikeCount()));
                    likeCount.setTextAppearance(activity, R.style.CommonTextStyle);
                    likeCount.setGravity(Gravity.CENTER);
                    postLikeContainer.addView(likeCount);

                    postInfoContainer.addView(postLikeContainer);

                    // 최종적으로 사용자 정보 섹터를 먼저 추가
                    postContainer.addView(postInfoContainer);

                    // 게시글 내용 추가
                    TextView postContent = new TextView(activity);
                    postContent.setBackgroundResource(R.drawable.common_container_style);
                    postContent.setText(post.getPostInfo().getContent());

                    // 텍스트에만 패딩을 설정 (왼쪽, 위, 오른쪽, 아래 순)
                    postContent.setPadding(convertDpToPx(8), convertDpToPx(8), convertDpToPx(8), convertDpToPx(8));

                    // 스타일 적용 (home.xml에서 정의한 스타일 적용)
                    postContent.setTextAppearance(activity, R.style.CommonTextStyle);  // 스타일 추가

                    // 레이아웃 파라미터 설정 (여백, 마진)
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(convertDpToPx(8), convertDpToPx(8), convertDpToPx(8), convertDpToPx(8));  // 여백 설정
                    postContent.setLayoutParams(layoutParams);
                    postContent.setHeight(convertDpToPx(100));

                    // 게시글 내용은 사용자 정보 밑에 추가
                    postContainer.addView(postContent);

                    // 최종적으로 postContainer를 화면에 추가
                    postOuterContainer.addView(postContainer);
                }
            }
        });
    }

    // dp를 px로 변환하는 메서드
    public int convertDpToPx(int dp) {
        // 화면 밀도를 가져옵니다.
        float density = getResources().getDisplayMetrics().density;
        // dp를 px로 변환하여 리턴합니다.
        return (int) (dp * density);
    }


    private void updateWeatherUI(WeatherResponse.Result result) {
        // 시간별 날씨 업데이트
        populateHourlyForecast(result.getHourlyWeatherData());
    }

//    private void loadWeatherData() {
//        String accessToken = ApplicationCofinguration.ACCESS_TOKEN;
//        String locationId = "703";
//
//        WeatherApi.fetchHourlyWeather(accessToken, locationId, new WeatherApi.WeatherCallback() {
//            @Override
//            public void onSuccess(WeatherResponse.Result result) {
//                // 데이터를 캐시
//                cachedWeatherData = result;
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(() -> populateHourlyForecast(result.getHourlyWeatherData()));
//
//                        // "구"와 "동" 텍스트 설정
//                        TextView currentDistrict = findViewById(R.id.currentDistrict);
//                        TextView currentTown = findViewById(R.id.currentTown);
//                        TextView currentTemp = findViewById(R.id.currentTemperature);
//                        ImageView weatherIcon = findViewById(R.id.weatherIcon);
//                        currentDistrict.setText(result.getCity());  // 구
//                        currentTown.setText(result.getStreet());    // 동
//                        currentTemp.setText(String.valueOf(result.getCurrentTmp()) + "°C");    // 현재 온도
//                        weatherIcon.setImageResource(getWeatherIcon(result.getCurrentSkyType()));   // 현재 하늘? 날씨? 상황
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.e("HomeActivity", "Error fetching hourly forecast: " + errorMessage);
//            }
//        });
//    }

//     더미 데이터를 로드하는 메서드
    private void loadWeatherData() {
        List<WeatherResponse.HourlyWeatherData> dummyData = createDummyHourlyData(); // 더미 데이터 생성
        populateHourlyForecast(dummyData); // 더미 데이터를 UI에 반영
    }

    private void populateHourlyForecast(List<WeatherResponse.HourlyWeatherData> weatherData) {
        hourlyForecastContainer.removeAllViews();

        for (WeatherResponse.HourlyWeatherData data : weatherData) {
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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);  // 텍스트 크기 (16sp)

        if (isBoldStyle) {
            textView.setTypeface(null, Typeface.BOLD);  // 볼드 텍스트
        }

        return textView;
    }

    @SuppressLint("SetTextI18n")
    private View createWeatherCard(WeatherResponse.HourlyWeatherData data) {
        CardView card = new CardView(this);

        // 둥근 모서리
        card.setRadius(10);
        card.setCardElevation(4);
        card.setBackground(null);       // 공통 컨테이너 스타일

        // 카드 간격 설정
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // w
                LinearLayout.LayoutParams.WRAP_CONTENT  // h
        );

        cardParams.setMargins(16, 16, 16, 16);

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(16, 16, 16, 16);
        cardContent.setBackground(createCommonBackgroundDrawable());    // 공통 컨테이너 스타일
        cardContent.setLayoutParams(cardParams);
        card.addView(cardContent);

        // 시간 표시
        TextView timeView = new TextView(this);
        timeView.setText(formatHour(data.getHour()));
        timeView.setTextSize(12);
        timeView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addCommonTextStyle(timeView, false);
        cardContent.addView(timeView);

        // 날씨 아이콘 추가
        ImageView weatherIcon = new ImageView(this);
        weatherIcon.setImageResource(getWeatherIcon(data.getSkyType())); // 아이콘 설정
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(100, 100); // 아이콘 크기
        iconParams.setMargins(0, 16, 0, 16);
        weatherIcon.setLayoutParams(iconParams);
        cardContent.addView(weatherIcon);

        // 강수량 표시
        TextView rainView = new TextView(this);
        rainView.setText(showText ? data.getRainText() : data.getRain() + "mm");
        rainView.setTextSize(12);
        rainView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addCommonTextStyle(rainView, false);
        cardContent.addView(rainView);

        // 온도 표시
        TextView tempView = new TextView(this);
        tempView.setText(showText ? data.getTmpText() : data.getTmp() + "°C");
        tempView.setTextSize(14);
        tempView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addCommonTextStyle(tempView, false);
        cardContent.addView(tempView);

        return card;
    }

    private String formatHour(String isoString) {
        return isoString.substring(11, 13) + "시"; // ISO 8601 시간 문자열에서 시간만 추출
    }

    private void loadAirQuality() {
        // Show air quality index
    }

    private int getWeatherIcon(String skyType) {
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
    private List<WeatherResponse.HourlyWeatherData> createDummyHourlyData() {
        List<WeatherResponse.HourlyWeatherData> dummyData = new ArrayList<>();
        dummyData.add(new WeatherResponse.HourlyWeatherData(
                "2024-08-19T07:00:00", "CLOUDY", "매우", "강함", 80, "", "추움", 6));
        dummyData.add(new WeatherResponse.HourlyWeatherData(
                "2024-08-19T08:00:00", "PARTLYCLOUDY", "매우", "강함", 90, "", "선선", 19));
        dummyData.add(new WeatherResponse.HourlyWeatherData(
                "2024-08-19T09:00:00", "CLEAR", "매우", "강함", 100, "", "추움", 6));
        dummyData.add(new WeatherResponse.HourlyWeatherData(
                "2024-08-19T10:00:00", "RAIN", "", "보통", 10, "", "더움", 34));
        dummyData.add(new WeatherResponse.HourlyWeatherData(
                "2024-08-19T11:00:00", "CLOUDY", "", "보통", 10, "", "더움", 34));
        dummyData.add(new WeatherResponse.HourlyWeatherData(
                "2024-08-19T12:00:00", "CLOUDY", "", "보통", 10, "", "더움", 34));
        dummyData.add(new WeatherResponse.HourlyWeatherData(
                "2024-08-19T13:00:00", "CLOUDY", "", "보통", 10, "", "더움", 34));

        return dummyData;
    }
}