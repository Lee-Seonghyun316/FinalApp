// MealGridAdapter.java
package com.example.finalapp;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MealGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Meal> mealList;

    public MealGridAdapter(Context context, ArrayList<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @Override
    public int getCount() {
        return mealList.size();
    }

    @Override
    public Object getItem(int position) {
        return mealList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // If convertView is null, inflate a new TextView
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(8, 8, 8, 8);
        } else {
            // If convertView is not null, reuse it
            textView = (TextView) convertView;
        }

        // Set the text of the TextView to display meal information
        Meal meal = mealList.get(position);
        textView.setText(meal.getDate() + "일" + "\n" + meal.getMenuName() + "\n" + meal.getCalories() + "cal");

        return textView;
    }
}
