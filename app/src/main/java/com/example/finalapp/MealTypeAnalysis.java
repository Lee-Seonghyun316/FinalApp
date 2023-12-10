package com.example.finalapp;

public class MealTypeAnalysis {
    private String type;
    private int totalCost;

    public MealTypeAnalysis(String type, int totalCost) {
        this.type = type;
        this.totalCost = totalCost;
    }

    public String getType() {
        return type;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void addToTotalCost(int cost) {
        this.totalCost += cost;
    }
}

