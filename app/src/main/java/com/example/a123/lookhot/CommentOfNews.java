package com.example.a123.lookhot;

import java.io.Serializable;

/**
 * Created by qhs on 2018/6/3 0003.
 */

public class CommentOfNews implements Serializable {
    private int id;         //回帖序号
    private int news_id;    //新闻序号
    private String pub_date;//回帖时间
    private String content; //回帖内容

    public CommentOfNews(){

    }

    public CommentOfNews(int id, int news_id, String pub_date, String content){
        this.id = id;
        this.news_id = news_id;
        this.pub_date = pub_date;
        this.content = content;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
