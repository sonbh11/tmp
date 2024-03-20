package com.example.tmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    private Button login, regist;
    private EditText input_StdNum, input_Pwd;
    private TextView ErrorMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("GuideBook");

        login = findViewById(R.id.Login);
        regist = findViewById(R.id.Regist);

        input_StdNum = findViewById(R.id.StdNumber);
        input_Pwd = findViewById(R.id.Password);

        ErrorMsg = findViewById(R.id.ErrorMsg);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String StdNum = input_StdNum.getText().toString();
                String Password = input_Pwd.getText().toString();

                run(StdNum, Password);
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

    }

    public void run(String StdNum, String Password){

        mDatabaseReference.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(StdNum).exists()){
                    String pwd = snapshot.child(StdNum).child("password").getValue(String.class);

                    if(Password.equals(pwd)){
                        MainActivity.stdNum = StdNum;
                        MainActivity.name = snapshot.child(StdNum).child("name").getValue(String.class);

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ErrorMsg.setText("학번과 비밀번호가 일치하지 않습니다.");
                    }
                } else {
                    ErrorMsg.setText("학번이 존재하지 않습니다.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Login", "Database error: Login");
            }
        });

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}