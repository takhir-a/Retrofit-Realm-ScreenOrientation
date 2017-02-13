package com.example.newsapi.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsEntity extends RealmObject {

    @PrimaryKey
    private int id;
    private String status;
    private String source;
    private String sortBy;

    public NewsEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

}