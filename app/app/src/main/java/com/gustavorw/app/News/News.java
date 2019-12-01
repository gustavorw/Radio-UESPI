package com.gustavorw.app.News;

public class News {
    private String titulo;
    private String data;
    private String link;
    public News(String title, String date, String link){
        this.titulo = title;
        this.data = date;
        this.link = link;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String title) {
        this.titulo = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String date) {
        this.data = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
