package com.example.finalapp;

import java.util.Date;

public class Meal {
    private String place;
    private byte[] imageBlob;
    private String menuName;
    private String rating;
    private Date time;
    private int cost;
    private int calories;
    private String type;

    // Getter 및 Setter 메서드 추가
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Meal(String place, byte[] imageBlob, String menuName, String rating, Date time, int cost, int calories, String type) {
        this.place = place;
        this.imageBlob = imageBlob;
        this.menuName = menuName;
        this.rating = rating;
        this.time = time;
        this.cost = cost;
        this.calories = calories;
        this.type = type;
    }
}

