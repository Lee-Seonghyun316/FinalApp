package com.example.finalapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MealAnalysisFragment extends Fragment {

    private TextView totalCaloriesTextView;
    private TextView costAnalysisTextView;

    private MealDatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meal_analysis, container, false);

        totalCaloriesTextView = root.findViewById(R.id.totalCaloriesTextView);
        costAnalysisTextView = root.findViewById(R.id.costAnalysisTextView);

        // Initialize database helper
        databaseHelper = new MealDatabaseHelper(requireContext());

        // Perform analysis
        performAnalysis();

        return root;
    }

    private void performAnalysis() {
        // Calculate the date one month ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthAgo = calendar.getTime();

        // Get meals for the last month
        ArrayList<Meal> mealsLastMonth = databaseHelper.getMealsSinceDate(oneMonthAgo);

        // Calculate total calories
        int totalCalories = calculateTotalCalories(mealsLastMonth);
        totalCaloriesTextView.setText("<최근 1달 간의 식사에 대한 칼로리 총량>" + "\n" + totalCalories + "cal");

        // Analyze cost by type
        String costAnalysis = analyzeCostByType(mealsLastMonth);
        costAnalysisTextView.setText("<최근 1달 간의 식사 비용을 종류별로 분석>" + costAnalysis);
    }

    private int calculateTotalCalories(ArrayList<Meal> meals) {
        int totalCalories = 0;
        for (Meal meal : meals) {
            totalCalories += meal.getCalories();
        }
        return totalCalories;
    }

    private String analyzeCostByType(ArrayList<Meal> meals) {
        // Create a map to store cost by type
        int breakfastCost = 0;
        int lunchCost = 0;
        int dinnerCost = 0;
        int drinkCost = 0;

        for (Meal meal : meals) {
            switch (meal.getType()) {
                case "조식":
                    breakfastCost += meal.getCost();
                    break;
                case "중식":
                    lunchCost += meal.getCost();
                    break;
                case "석식":
                    dinnerCost += meal.getCost();
                    break;
                case "음료":
                    drinkCost += meal.getCost();
                    break;
            }
        }

        // Create a summary string
        return  "\n" + "- 조식: " + breakfastCost + "\n" +
                "- 중식: " + lunchCost + "\n" +
                "- 석식: " + dinnerCost + "\n" +
                "- 음료: " + drinkCost;
    }
}
