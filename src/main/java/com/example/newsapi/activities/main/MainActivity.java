package com.example.newsapi.activities.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newsapi.R;
import com.example.newsapi.activities.web.WebActivity;
import com.example.newsapi.api.APIBuilder;
import com.example.newsapi.dto.Article;
import com.example.newsapi.dto.News;
import com.example.newsapi.eventBusSignatures.NetworkChange;
import com.example.newsapi.realm.RealmService;
import com.example.newsapi.realm.RealmServiceImpl;
import com.example.newsapi.utils.AppUtils;
import com.example.newsapi.utils.RvAdapterClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout srlMain;
    RecyclerView rvNewsList;
    AlertDialog alertDialog;
    Snackbar snackbar;

    LinearLayoutManager rvNewsListManager;
    RecyclerView.Adapter rvNewsListAdapter;
    List<Article> articleList;

    RealmService realmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realmService = new RealmServiceImpl();
        rvNewsList = (RecyclerView) findViewById(R.id.rv_news_list);
        srlMain = (SwipeRefreshLayout) findViewById(R.id.srl_main);
        srlMain.setOnRefreshListener(this);
        initRvNewsList();
        getNewsFromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        getNewsFromApi();
    }

    public void initRvNewsList() {
        articleList = new ArrayList<>();
        rvNewsListManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvNewsList.setLayoutManager(rvNewsListManager);
        rvNewsListAdapter = new RvNewsListAdapter(articleList, MainActivity.this);
        rvNewsList.setAdapter(rvNewsListAdapter);
        rvNewsList.addOnItemTouchListener(new RvAdapterClickListener(MainActivity.this, new RvAdapterClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (AppUtils.internetIsEnabled()){
                    Intent webActivity = new Intent(MainActivity.this, WebActivity.class);
                    webActivity.putExtra(WebActivity.NEWS_URL, articleList.get(position).getUrl());
                    startActivity(webActivity);
                }else {
                    openDialog("Internet Issue", "Please switch on internet");
                }
            }
        }));
    }

    public void getNewsFromDB(){
        News news = realmService.getNews();
        if (news!=null) {
            updateNewsList(news);
        }else {
            getNewsFromApi();
        }
    }

    public void getNewsFromApi(){
        if (AppUtils.internetIsEnabled()){
            progressBarVisible();
            APIBuilder.apiService.getNewsLatest().enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.body()!=null){
                        News news = response.body();
                        realmService.clearDateBase();
                        realmService.saveNews(news);
                        articleList.clear();
                        updateNewsList(news);
                    }
                    progressBarInvisible();
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    snackbarShow("Error with connection");
                    progressBarInvisible();
                }
            });
        }else {
            progressBarInvisible();
            openDialog("Internet Issue", "Please switch on internet");
        }
    }

    public void updateNewsList(News news){
        for (Article article : news.getArticles()){
            articleList.add(article);
        }
        rvNewsListAdapter.notifyDataSetChanged();
    }


    public void openDialog(String title, String message) {
        String positiveText = "SETTINGS";
        String negativeText = "LATER";
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.provider.Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public void closeDialog() {
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
    }

    public void snackbarShow(String message) {
        snackbar = Snackbar
                .make(srlMain, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void progressBarVisible() {
        srlMain.setRefreshing(true);
    }

    public void progressBarInvisible() {
        srlMain.setRefreshing(false);
    }

    @Subscribe
    public void NetworkChangeEvent(NetworkChange event){
        closeDialog();
    }
}