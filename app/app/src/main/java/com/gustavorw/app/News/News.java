package com.gustavorw.app.News;

public class News {
    private String title;
    private String date;
    private String link;
    public News(String title, String date, String link){
        this.title = title;
        this.date = date;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
