package com.example.newsapi.realm;

import com.example.newsapi.dto.Article;
import com.example.newsapi.dto.News;
import com.example.newsapi.entities.ArticleEntity;
import com.example.newsapi.entities.NewsEntity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by admin on 12/28/2016.
 */

public class RealmServiceImpl implements RealmService {

    Realm realmDB = Realm.getDefaultInstance();


    @Override
    public void clearDateBase() {
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NewsEntity newsEntity = realmDB.where(NewsEntity.class).findFirst();
                RealmResults<ArticleEntity> articleEntities = realmDB.where(ArticleEntity.class).findAll();
                if (newsEntity!=null) {
                    newsEntity.deleteFromRealm();
                }
                if (articleEntities!=null) {
                    articleEntities.deleteAllFromRealm();
                }
            }
        });
    }

    @Override
    public void saveNews(News news) {
        int id = 0;
        realmDB.beginTransaction();
        NewsEntity newsEntity = realmDB.createObject(NewsEntity.class, id);
        newsEntity.setSortBy(news.getSortBy());
        newsEntity.setSource(news.getSource());
        newsEntity.setStatus(news.getStatus());
        realmDB.commitTransaction();

        saveArticles(news.getArticles());
    }

    public void saveArticles(List<Article> articleList) {
        int id = 0;
        realmDB.beginTransaction();
        for (Article article : articleList) {
            ArticleEntity articleEntity = realmDB.createObject(ArticleEntity.class, id);
            articleEntity.setAuthor(article.getAuthor());
            articleEntity.setDescription(article.getDescription());
            articleEntity.setTitle(article.getTitle());
            articleEntity.setUrl(article.getUrl());
            articleEntity.setUrlToImage(article.getUrlToImage());
            articleEntity.setPublishedAt(article.getPublishedAt());
            id = id + 1;
        }
        realmDB.commitTransaction();
    }

    @Override
    public News getNews() {
        NewsEntity newsEntity = realmDB.where(NewsEntity.class).findFirst();
        if (newsEntity!=null) {
            News news = new News();
            news.setStatus(newsEntity.getStatus());
            news.setSource(newsEntity.getSource());
            news.setSortBy(newsEntity.getSortBy());
            List<Article> articleList = getArticles();
            if (articleList!=null){
                news.setArticles(articleList);
            }else {
                news.setArticles(new ArrayList<Article>());
            }
            return news;
        }
        return null;
    }

    public List<Article> getArticles() {
        RealmResults<ArticleEntity> articleEntities = realmDB.where(ArticleEntity.class).findAll();
        if (articleEntities!=null) {
            List<Article> articleList = new ArrayList<>();
            for (ArticleEntity articleEntity : articleEntities){
                Article article = new Article();
                article.setAuthor(articleEntity.getAuthor());
                article.setDescription(articleEntity.getDescription());
                article.setTitle(articleEntity.getTitle());
                article.setUrl(articleEntity.getUrl());
                article.setUrlToImage(articleEntity.getUrlToImage());
                article.setPublishedAt(articleEntity.getPublishedAt());
                articleList.add(article);
            }
            return articleList;
        }
        return null;
    }

}