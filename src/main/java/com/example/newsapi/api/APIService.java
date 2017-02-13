package com.example.newsapi.api;

import com.example.newsapi.dto.News;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("articles?source=the-next-web&sortBy=latest&apiKey=9f4560e2c7024099a8ca269fde2af57f")
    Call<News> getNewsLatest();

    @GET("articles?source=the-next-web&sortBy=top&apiKey=9f4560e2c7024099a8ca269fde2af57f")
    Call<News> getNewsTop();

    @GET("articles?source=the-next-web&sortBy=popular&apiKey=9f4560e2c7024099a8ca269fde2af57f")
    Call<News> getNewsPopular();
}