package com.example.tmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CompletableFuture;

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

        //**************************************************************수정내역
        departmentSpinner = findViewById(R.id.department_spinner);
        //**************************************************************수정내역

        input_stdnum = findViewById(R.id.id_input);
        input_pwd = findViewById(R.id.pwd_input);
        input_pwdcheck = findViewById(R.id.confirm_input);
        input_name = findViewById(R.id.input_name);

        name_error = findViewById(R.id.name_error);
        std_error = findViewById(R.id.std_error);
        pwd_error = findViewById(R.id.pwd_error);

        register = findViewById(R.id.register_button);
        cancel = findViewById(R.id.cancel_button);

        //**************************************************************수정내역
        //- res/values/departments.xml 파일 내에 배열로 선언된 학과리스트 가져오기
        Resources res = getResources();
        // 가져와서 departments에 저장
        String[] departments = res.getStringArray(R.array.department_list);
        // 어댑터로 스피너에 학과정보 추가
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter);
        //**************************************************************수정내역


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String StdNum = input_stdnum.getText().toString();
                String Pwd = input_pwd.getText().toString();
                String Pwd_check = input_pwdcheck.getText().toString();
                String Name = input_name.getText().toString();
                String selectedDepartment = departmentSpinner.getSelectedItem().toString();

                SignUp(Name, StdNum, Pwd, Pwd_check,selectedDepartment);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    public void SignUp(String Name, String StdNum, String Pwd, String Pwd_check,String Department){
        mDatabaseReference.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean check = true;

                if(snapshot.child(StdNum).exists()){
                    std_error.setText("이미 등록됨");
                    check = false;
                }else std_error.setText("");

                if(confirmation(Name, StdNum, Pwd, Pwd_check) && check) {
                    UserAccount user = new UserAccount();
                    user.setName(Name);
                    user.setStdNum(StdNum);
                    user.setPassword(Pwd);
                    user.setDepartment(Department);

                    mDatabaseReference.child("UserAccount").child(StdNum).setValue(user);

                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Register", "Database error: Register");
            }
        });
    }

    public boolean confirmation(String Name, String StdNum, String Pwd, String Pwd_check){
        boolean check = true;

        if (!(isNumeric(StdNum) && StdNum.length() == 8)) {
            std_error.setText("올바르지 않음");
            check = false;
        } else {
            if(!std_error.getText().toString().equals("이미 등록됨"))
                std_error.setText("");
        }

        if (!(Pwd.equals(Pwd_check))){
            pwd_error.setText("올바르지 않음");
            check = false;
        } else pwd_error.setText("");

        if (Name.length() > 6){
            name_error.setText("글자수 초과");
            check = false;
        } else name_error.setText("");

        return check;
    }

}