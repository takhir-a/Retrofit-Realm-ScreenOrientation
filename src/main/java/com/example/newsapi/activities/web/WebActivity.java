package com.example.newsapi.activities.web;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import com.example.newsapi.R;

public class WebActivity extends AppCompatActivity {


    public static String NEWS_URL;
    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wvNews = (WebView) findViewById(R.id.wv_news);

        if (wvNews!=null) {
            wvNews.getSettings().setUserAgentString("news-api");
            wvNews.getSettings().setJavaScriptEnabled(true);
            wvNews.postUrl(getIntent().getStringExtra(NEWS_URL), null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
