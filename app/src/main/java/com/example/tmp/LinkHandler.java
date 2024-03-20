package com.example.tmp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LinkHandler {

    public static void handleLink1(Context context) {
        String link = "https://portal.yu.ac.kr";
        openWebViewActivity(context, link);
    }

    public static void handleLink2(Context context) {
        String link = "https://hcms.yu.ac.kr/main/intro/campus-map.do";
        openWebViewActivity(context, link);
    }

    //    public static void handleLink3(Context context) {
//        String link = "https://cse.yu.ac.kr";
//        openWebViewActivity(context,link);
//    }
//디비조회해서 현재 접속자 학과에 따라 해당하는 학과홈페이지 링크
    public static void handleLink3(Context context, String uid) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("GuideBook").child("UserAccount").child(uid);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String department = dataSnapshot.child("department").getValue(String.class);
                    Log.d("User Info", "Name: " + name + ", Department: " + department); // 확인을 위한 로그 추가

                    String link = "";
                    if ("cse".equals(department)) {
                        link = "https://cse.yu.ac.kr";
                        openWebViewActivity(context, link);
                    } else if ("ice".equals(department)) {
                        link = "https://ice.yu.ac.kr/ice/index.do";
                        openWebViewActivity(context, link);
                    } else if ("sw".equals(department)) {
                        link = "https://sw.yu.ac.kr/sw/index.do";
                        openWebViewActivity(context, link);
                    } else {
                        link = "https://naver.com"; // 학과에 해당하지 않는 경우
                        openWebViewActivity(context, link);
                    }
                } else {
                    Log.d("User Info", "User not found"); // 사용자 정보가 없는 경우
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LingHandler", "Failed to read value.", databaseError.toException());
            }
        });
    }



    private static void openWebViewActivity(Context context, String link) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("link", link);
        context.startActivity(intent);
    }
}
