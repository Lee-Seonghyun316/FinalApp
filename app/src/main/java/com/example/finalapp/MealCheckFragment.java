package com.example.finalapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.finalapp.Meal;
import com.example.finalapp.MealListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class MealCheckFragment extends Fragment {

    private FrameLayout container;
    private ToggleButton toggleButton;
    private ListView listView;
    private MealListAdapter mealListAdapter;
    private MealDatabaseHelper databaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_check, container, false);

        this.container = view.findViewById(R.id.container);
        toggleButton = view.findViewById(R.id.toggleButton);

        // MealDatabaseHelper 초기화
        databaseHelper = new MealDatabaseHelper(requireContext());

        // 초기 화면 설정
        showList();

        // 토글 버튼 리스너 설정
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showList();
            } else {
                showNovemberMenu();
            }
        });

        return view;
    }


    private void showList() {
        // 리스트 레이아웃을 인플레이트하여 추가
        container.removeAllViews(); // 기존의 뷰를 제거
        View listLayout = getLayoutInflater().inflate(R.layout.list_layout, container, false);
        container.addView(listLayout);

        // 데이터베이스에서 모든 식사 데이터 가져오기
        ArrayList<Meal> mealList = databaseHelper.getAllMeals();

        // 리스트뷰에 어댑터 설정
        mealListAdapter = new MealListAdapter(requireContext(), mealList);
        ListView listView = listLayout.findViewById(R.id.listView);
        listView.setAdapter(mealListAdapter);
    }


    private void showNovemberMenu() {
        container.removeAllViews(); // Remove existing views

        // Inflate the November layout
        View novemberLayout = getLayoutInflater().inflate(R.layout.november_layout, container, false);
        container.addView(novemberLayout);

        // Get and display November meals
        ArrayList<Meal> novemberMeals = getNovemberMeals();
        GridView gridView = novemberLayout.findViewById(R.id.gridView);
        MealGridAdapter gridAdapter = new MealGridAdapter(requireContext(), novemberMeals);
        gridView.setAdapter(gridAdapter);

        // Display "11월" at the top
        TextView monthTextView = novemberLayout.findViewById(R.id.monthTextView);
        monthTextView.setText("11월");
    }

    private ArrayList<Meal> getNovemberMeals() {
        // 식단 데이터베이스에서 11월에 해당하는 데이터 가져오기
        ArrayList<Meal> allMeals = databaseHelper.getAllMeals();
        ArrayList<Meal> novemberMeals = new ArrayList<>();

        // 현재 월과 11월을 비교하여 일치하는 데이터만 추출
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); // 현재 월 (0-based)
        int november = Calendar.NOVEMBER; // 11월 (0-based)

        for (Meal meal : allMeals) {
            calendar.setTime(meal.getTime());
            if (calendar.get(Calendar.MONTH) == november) {
                novemberMeals.add(meal);
            }
        }

        return novemberMeals;
    }

}
