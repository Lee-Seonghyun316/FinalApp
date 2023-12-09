package com.example.finalapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class MealCheckFragment extends Fragment {

    private ListView listView;
    private MealListAdapter mealListAdapter;
    private MealDatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meal_check, container, false);
        listView = root.findViewById(R.id.listView);

        // MealDatabaseHelper 초기화
        databaseHelper = new MealDatabaseHelper(requireContext());

        // 데이터베이스에서 모든 식사 데이터 가져오기
        ArrayList<Meal> mealList = databaseHelper.getAllMeals();

        // 리스트뷰에 어댑터 설정
        mealListAdapter = new MealListAdapter(requireContext(), mealList);
        listView.setAdapter(mealListAdapter);

        return root;
    }
}
