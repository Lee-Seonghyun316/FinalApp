// MealGridAdapter.java
package com.example.finalapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MealGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Meal> mealList;

    public MealGridAdapter(Context context, ArrayList<Meal> mealList) {
        this.context = context;

        // mealList를 오래된 순으로 정렬
        Collections.sort(mealList, new Comparator<Meal>() {
            @Override
            public int compare(Meal meal1, Meal meal2) {
                return meal1.getDate().compareTo(meal2.getDate());
            }
        });

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
        View gridViewItem = convertView;

        if (gridViewItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridViewItem = inflater.inflate(R.layout.november_layout, parent, false);
        }

        // 여기에서 각 요소의 내용을 설정하세요
        TextView itemText = gridViewItem.findViewById(R.id.textView);
        Meal meal = mealList.get(position);
        itemText.setText(meal.getDate() + "일" + "\n" + meal.getMenuName() + "\n" + meal.getCalories() + "cal");

        return gridViewItem;
    }

}
