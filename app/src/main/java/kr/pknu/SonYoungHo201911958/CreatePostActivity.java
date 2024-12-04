package kr.pknu.SonYoungHo201911958;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.pknu.SonYoungHo201911958.api.PostApi;
import kr.pknu.SonYoungHo201911958.config.ApplicationCofinguration;

public class CreatePostActivity extends AppCompatActivity {

    private EditText contentEditText;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        contentEditText = findViewById(R.id.contentEditText);  // 글 내용 입력 EditText
        shareButton = findViewById(R.id.shareButton);          // 공유하기 버튼

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 글 작성 후 API 요청 보내기
                String content = contentEditText.getText().toString();
                sharePost(content);
            }
        });
    }

    private void sharePost(String content) {
        PostApi.sharePost(ApplicationCofinguration.ACCESS_TOKEN, content, new PostApi.CreatePostCallback() {
            @Override
            public void onSuccess(String message) {
                // 성공 시, 원래 화면으로 돌아가 게시글 로드
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreatePostActivity.this, message, Toast.LENGTH_SHORT).show();
                        loadPosts();
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // 에러 처리
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreatePostActivity.this, "오류: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadPosts() {
        // 원래 화면으로 돌아가 게시글을 새로 불러오는 작업
        Intent intent = new Intent(CreatePostActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
