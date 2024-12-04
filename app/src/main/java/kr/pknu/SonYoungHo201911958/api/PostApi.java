package kr.pknu.SonYoungHo201911958.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.pknu.SonYoungHo201911958.config.ApplicationCofinguration;
import kr.pknu.SonYoungHo201911958.dto.PostResponse;

public class PostApi {
    private static final String BASE_URL = ApplicationCofinguration.URL;

    public interface PostCallback {
        void onSuccess(List<PostResponse.Post> posts);
        void onError(String errorMessage);
    }

    public static void getPosts(int lastPostId, int size, String postType, int locationId, PostCallback callback) {
        new Thread(() -> {
            try {
                String urlString = BASE_URL + "/api/v1/community/posts?lastPostId=" + lastPostId + "&size=" + size + "&postType=" + postType + "&locationId=" + locationId;

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + ApplicationCofinguration.ACCESS_TOKEN);

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
                        JSONArray postList = result.getJSONArray("postList");
                        List<PostResponse.Post> posts = new ArrayList<>();

                        for (int i = 0; i < postList.length(); i++) {
                            JSONObject postObject = postList.getJSONObject(i);
                            JSONObject postInfo = postObject.getJSONObject("postInfo");
                            JSONObject memberInfo = postObject.getJSONObject("memberInfo");

                            PostResponse.Post post = new PostResponse.Post(new PostResponse.PostInfo(
                                    postInfo.getInt("postId"),
                                    postInfo.getString("content"),
                                    postInfo.getString("createdAt"),
                                    postInfo.getInt("likeCount"),
                                    postInfo.getBoolean("likeClickable")
                                    ), new PostResponse.MemberInfo(
                                            memberInfo.getString("memberName"),
                                            memberInfo.getString("profileImageUrl"),
                                            memberInfo.getString("sensitivity"),
                                            memberInfo.getString("city"),
                                            memberInfo.getString("street")
                                    )
                            );
                            posts.add(post);
                        }

                        callback.onSuccess(posts);
                    } else {
                        callback.onError(jsonResponse.getString("message"));
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }
            } catch (Exception e) {
                Log.e("PostApi", "Error fetching posts data", e);
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public interface CreatePostCallback {
        void onSuccess(String message);

        void onError(String errorMessage);
    }

    public static void sharePost(String accessToken, String content, CreatePostCallback callback) {
        new Thread(() -> {
            try {
                String apiUrl = BASE_URL + "/api/v1/post";  // 실제 API 엔드포인트로 변경

                // 요청 Body에 포함될 데이터 설정
                JSONObject requestBody = new JSONObject();
                requestBody.put("content", content);
                requestBody.put("temperatureTagCode", 1);
                requestBody.put("skyTagCode", 1);
                requestBody.put("humidityTagCode", 1);
                requestBody.put("windTagCode", 1);
                requestBody.put("dustTagCode", 1);

                // URL 객체 생성
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + ApplicationCofinguration.ACCESS_TOKEN);
                connection.setDoOutput(true);

                // 요청 Body 전송
                OutputStream os = connection.getOutputStream();
                os.write(requestBody.toString().getBytes());
                os.flush();
                os.close();

                // 응답 처리
                int responseCode = connection.getResponseCode();
                BufferedReader reader;
                StringBuilder response = new StringBuilder();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    Log.d("PostApi", "Response: " + response.toString());

                    // 응답 처리
                    if (jsonResponse.getBoolean("isSuccess")) {
                        callback.onSuccess("정상적인 요청.");
                    } else {
                        callback.onError(jsonResponse.getString("message"));
                    }
                } else {
                    callback.onError("HTTP error code: " + responseCode);
                }

            } catch (Exception e) {
                Log.e("PostApi", "Error sharing post", e);
                callback.onError(e.getMessage());
            }
        }).start();
    }
}
