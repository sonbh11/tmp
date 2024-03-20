package com.example.tmp;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardMonitor {

    private OnCompleteListener mListener;

    public BoardMonitor(OnCompleteListener listener) {
        this.mListener = listener;
    }

    public void monitorBoard(String boardUrl) {
        new MonitorBoardTask().execute(boardUrl);
    }

    public interface OnCompleteListener {
        void onComplete(List<Map<String, String>> posts);
    }

    private class MonitorBoardTask extends AsyncTask<String, Void, List<Map<String, String>>> {

        @Override
        protected List<Map<String, String>> doInBackground(String... urls) {
            List<Map<String, String>> posts = new ArrayList<>();

            try {
                // 영대홈피에서 게시글 가져오기
                Document doc = Jsoup.connect(urls[0]).get();

                // 목록 가져오기
                Elements postElements = doc.select("div.b-title-box");

                // 가져온 게시글 처리
                for (Element post : postElements) {
                    //제목
                    String title = post.select("span").get(1).text();

                    //링크
                    String link = "https://www.yu.ac.kr/main/bachelor/bachelor-guide.do" + post.select("a").attr("href");

                    //제목, 링크 map에 추가 ( 다른 정보들 들고오고 싶으면 css 선택자로 직접 들고오면됨.)
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("title", title);
                    postMap.put("link", link);
                    posts.add(postMap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return posts;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> posts) {
            super.onPostExecute(posts);
            if (mListener != null) {
                mListener.onComplete(posts);
            }
        }
    }
}
