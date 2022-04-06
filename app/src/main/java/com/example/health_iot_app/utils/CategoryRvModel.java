package com.example.health_iot_app.utils;

public class CategoryRvModel {
    private int image;
    private String text;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CategoryRvModel(int image, String text) {
        this.image = image;
        this.text = text;
    }
}
