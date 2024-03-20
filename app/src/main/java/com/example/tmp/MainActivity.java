package com.example.tmp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button btn_urp;
    private Button btn_department;
    private Button btn_campus_map;
    private Button btn_haksa_info;
    private DatabaseReference mDatabaseReference;

    public static String stdNum;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 사용자 정보 가져오기
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // 사용자가 로그인되어 있는 경우
            String email = currentUser.getEmail();
            stdNum = email != null ? email.substring(0, email.indexOf('@')) : null; // 이메일에서 학번 추출
            Log.d("MainActivity", "Student Number: " + stdNum);
        } else {
            // 사용자가 로그인되어 있지 않은 경우
            // 로그인 화면으로 이동하도록 처리
            startActivity(new Intent(MainActivity.this, Login.class));
            finish(); // 현재 액티비티 종료
            return; // onCreate 메서드 종료
        }

        // 버튼 초기화
        btn_urp = findViewById(R.id.btn_urp);
        btn_department = findViewById(R.id.btn_department);
        btn_campus_map = findViewById(R.id.btn_campus_map);
        btn_haksa_info = findViewById(R.id.btn_haksa_info);

        // 학사정보 버튼 클릭 이벤트 처리
        btn_haksa_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BoardMonitorActivity.class));
            }
        });

        // 웹뷰 핸들러
        btn_urp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "https://portal.yu.ac.kr";
                openWebViewActivity(link);
            }
        });
        btn_campus_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "https://hcms.yu.ac.kr/main/intro/campus-map.do";
                openWebViewActivity(link);
            }
        });
        btn_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDepartmentLink();
            }
        });
    }

    private void openWebViewActivity(String link) {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    private void handleDepartmentLink() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("GuideBook/UserAccount/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/department");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String department = dataSnapshot.getValue(String.class);
                    Log.d("Main", "department : " + department);
                    String link = "";
                    if ("cse".equals(department)) {
                        link = "https://cse.yu.ac.kr";
                    } else if ("ice".equals(department)) {
                        link = "https://ice.yu.ac.kr/ice/index.do";
                    } else if ("sw".equals(department)) {
                        link = "https://sw.yu.ac.kr/sw/index.do";
                    } else {
                        link = "https://naver.com"; // 학과에 해당하지 않는 경우
                    }
                    openWebViewActivity(link);
                } else {
                    Log.d("Main", "Department not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Main", "Failed to read department: " + databaseError.getMessage());
            }
        });
    }

}
