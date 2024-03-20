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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button login, regist;
    private EditText input_StdNum, input_Pwd;
    private TextView ErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

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

                signIn(StdNum, Password);
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

    private void signIn(String StdNum, String Password) {
        String email = StdNum + "@example.com"; // 학번을 이메일 주소의 로컬 파트로 사용
        mAuth.signInWithEmailAndPassword(email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                String name = user.getDisplayName();
                                MainActivity.stdNum = StdNum;
                                MainActivity.name = name;

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
