package com.example.newsapi.realm;


import com.example.newsapi.dto.Article;
import com.example.newsapi.dto.News;

import java.util.List;

/**
 * Created by admin on 12/28/2016.
 */

public interface RealmService {
    void clearDateBase();
    void saveNews(News news);
    News getNews();
}
