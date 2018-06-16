package com.example.administrator.themeanime.adapter;

/**
 * Created by Administrator on 26/12/2017.
 */

public class ItemListView {
    private String name;
    private String url;

    public ItemListView(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
