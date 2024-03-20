package com.example.tmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private EditText input_name, input_stdnum, input_pwd, input_pwdcheck;
    private Button register, cancel;
    private Spinner departmentSpinner;
    private TextView name_error, std_error, pwd_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("GuideBook");

        departmentSpinner = findViewById(R.id.department_spinner);
        input_stdnum = findViewById(R.id.id_input);
        input_pwd = findViewById(R.id.pwd_input);
        input_pwdcheck = findViewById(R.id.confirm_input);
        input_name = findViewById(R.id.input_name);

        name_error = findViewById(R.id.name_error);
        std_error = findViewById(R.id.std_error);
        pwd_error = findViewById(R.id.pwd_error);

        register = findViewById(R.id.register_button);
        cancel = findViewById(R.id.cancel_button);

        // 학과(부서) 스피너 설정
        setupDepartmentSpinner();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stdNum = input_stdnum.getText().toString();
                String pwd = input_pwd.getText().toString();
                String pwdCheck = input_pwdcheck.getText().toString();
                String name = input_name.getText().toString();
                String selectedDepartment = departmentSpinner.getSelectedItem().toString();

                signUp(name, stdNum, pwd, pwdCheck, selectedDepartment);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupDepartmentSpinner() {
        Resources res = getResources();
        String[] departments = res.getStringArray(R.array.department_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);
    }

    private void signUp(String name, String stdNum, String pwd, String pwdCheck, String department) {
        mFirebaseAuth.createUserWithEmailAndPassword(stdNum + "@example.com", pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 사용자가 성공적으로 등록되었으면 사용자 정보를 데이터베이스에 저장
                            UserAccount user = new UserAccount();
                            user.setName(name);
                            user.setStdNum(stdNum);
                            user.setPassword(pwd);
                            user.setDepartment(department);

                            mDatabaseReference.child("UserAccount").child(stdNum).setValue(user);

                            Toast.makeText(Register.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 사용자 등록에 실패한 경우
                            Toast.makeText(Register.this, "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean validateInput(String name, String stdNum, String pwd, String pwdCheck) {
        boolean isValid = true;

        if (name.isEmpty()) {
            name_error.setText("이름을 입력하세요.");
            isValid = false;
        } else {
            name_error.setText("");
        }

        if (stdNum.isEmpty() || stdNum.length() != 8) {
            std_error.setText("올바른 학번을 입력하세요.");
            isValid = false;
        } else {
            std_error.setText("");
        }

        if (pwd.isEmpty() || pwd.length() < 6) {
            pwd_error.setText("비밀번호는 6자 이상이어야 합니다.");
            isValid = false;
        } else if (!pwd.equals(pwdCheck)) {
            pwd_error.setText("비밀번호가 일치하지 않습니다.");
            isValid = false;
        } else {
            pwd_error.setText("");
        }

        return isValid;
    }
}
