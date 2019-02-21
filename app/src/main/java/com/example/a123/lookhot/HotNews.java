package com.example.a123.lookhot;

/**
 * Created by 123 on 2018/5/31.
 */

public class HotNews {

    private int _id;
    private String news_title;
    private String news_type;
    private String publish_date;
    private int comment_count;
    private  String publish_house;
    private String news_content;

    public HotNews(int _id, String news_title, int comment_count, String news_type
                   ){
        super();
        this._id=_id;
        this.news_title=news_title;
        this.comment_count=comment_count;
        this.news_type=news_type;

    }

    public HotNews(int _id, String news_title, String publish_date, String publish_house, String news_content){
        super();
        this._id=_id;
        this.news_title=news_title;
        this.publish_date=publish_date;
        this.publish_house=publish_house;
        this.news_content=news_content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_type() {
        return news_type;
    }

    public void setNews_type(String news_type) {
        this.news_type = news_type;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getPublish_house() {
        return publish_house;
    }

    public void setPublish_house(String publish_house) {
        this.publish_house = publish_house;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }
}
