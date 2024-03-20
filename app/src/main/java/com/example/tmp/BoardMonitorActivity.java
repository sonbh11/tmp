package com.example.tmp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardMonitorActivity extends AppCompatActivity {

    private ListView listView;
    private List<Map<String, String>> postList;
    private WebView webView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_monitor);

        listView = findViewById(R.id.haksaList);
        webView = findViewById(R.id.haksainfo);

        // 게시판 모니터링
        BoardMonitor boardMonitor = new BoardMonitor(new BoardMonitor.OnCompleteListener() {
            @Override
            public void onComplete(List<Map<String, String>> posts) {
                postList = posts;
                // 게시글 제목 파싱
                final List<String> postTitles = new ArrayList<>();
                for (Map<String, String> post : posts) {
                    postTitles.add(post.get("title"));
                }

                // 어댑터 생성 , 리스트뷰에 연결
                adapter = new ArrayAdapter<>(BoardMonitorActivity.this, android.R.layout.simple_list_item_1, postTitles);
                listView.setAdapter(adapter);
            }
        });
        boardMonitor.monitorBoard("https://www.yu.ac.kr/main/bachelor/bachelor-guide.do");

        //게시글 클릭시 웹뷰열기
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String link = postList.get(position).get("link");
            Intent intent = new Intent(BoardMonitorActivity.this, WebViewActivity.class);
            intent.putExtra("link", link);
            startActivity(intent);
        });
    }
}
