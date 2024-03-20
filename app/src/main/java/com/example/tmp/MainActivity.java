package com.example.tmp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // 현재 사용자가 로그인되어 있는 경우
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();
            // 이후 필요한 작업 수행
            Log.d("MainActivity", "UID: " + uid);
            Log.d("MainActivity", "Email: " + email);
        } else {
            // 현재 사용자가 로그인되어 있지 않은 경우
            // 로그인 화면으로 이동하도록 처리
            startActivity(new Intent(MainActivity.this, Login.class));
            finish(); // 현재 액티비티 종료
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
                LinkHandler.handleLink1(MainActivity.this);
            }
        });
        btn_campus_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkHandler.handleLink2(MainActivity.this);
            }
        });
        btn_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkHandler.handleLink3(MainActivity.this, stdNum);
            }
        });
    }
}
