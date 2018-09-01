package com.piramidsoft.sirs.Model;

public class MenuModel {
    private int images;
    private String title;

    public MenuModel() {
    }

    public MenuModel(int images, String title) {
        this.images = images;
        this.title = title;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
