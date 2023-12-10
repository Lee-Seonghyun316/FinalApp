package com.example.finalapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MealListAdapter extends ArrayAdapter<Meal> {

    private final Context context;
    private final ArrayList<Meal> mealList;
    // 체크된 아이템들의 칼로리 합계를 계산하는 변수
    private int totalCalories = 0;

    // 체크된 아이템의 position을 저장하는 리스트
    private ArrayList<Integer> checkedItems = new ArrayList<>();

    public MealListAdapter(Context context, ArrayList<Meal> mealList) {
        super(context, R.layout.meal_list_item, mealList);
        this.context = context;
        // mealList를 역순으로 정렬
        Collections.reverse(mealList);
        this.mealList = mealList;
    }

// ...

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.meal_list_item, parent, false);

        // 뷰 초기화
        ImageView imageView = rowView.findViewById(R.id.imageView);
        TextView menuNameTextView = rowView.findViewById(R.id.menuNameTextView);
        TextView dateTextView = rowView.findViewById(R.id.dateTextView);

        // 현재 위치의 Meal 객체 가져오기
        Meal meal = mealList.get(position);

        // 이미지 설정
        byte[] imageBlob = meal.getImageBlob();
        if (imageBlob != null && imageBlob.length > 0) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length));
        } else {
            // 이미지가 없는 경우 기본 이미지 설정
            imageView.setImageResource(R.drawable.default_image);
        }

        // 메뉴명 설정
        menuNameTextView.setText(meal.getMenuName());

        // 날짜 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(meal.getTime());
        dateTextView.setText(formattedDate);

        // 리스트 아이템 클릭 시 상세페이지로 이동
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MealDetailActivity로 이동하는 코드 추가
                try {
                    Intent detailIntent = new Intent(context, MealDetailActivity.class);
                    detailIntent.putExtra("meal", meal);
                    context.startActivity(detailIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("onClick", "error", e);
                }

            }
        });

        // 체크박스 설정
        CheckBox checkBox = rowView.findViewById(R.id.checkBox);
        TextView caloriesTextView = rowView.findViewById(R.id.caloriesTextView);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 체크 상태가 변경되었을 때의 처리
            if (isChecked) {
                // 체크된 경우
                checkedItems.add(position); // 체크된 아이템의 position을 리스트에 추가
                totalCalories += meal.getCalories(); // 해당 아이템의 칼로리를 합계에 추가
            } else {
                // 체크가 해제된 경우
                checkedItems.remove((Integer) position); // 체크가 해제된 아이템의 position을 리스트에서 제거
                totalCalories -= meal.getCalories(); // 해당 아이템의 칼로리를 합계에서 빼기
            }
            // 합계를 TextView에 업데이트
            updateTotalCalories();
        });
        caloriesTextView.setText(String.valueOf(meal.getCalories()) + "cal 합산");
        return rowView;
    }

    // 체크된 아이템들의 칼로리 합계를 업데이트하고 TextView에 표시
    private void updateTotalCalories() {
        TextView totalCaloriesTextView = ((Activity) context).findViewById(R.id.totalCaloriesTextView);
        totalCaloriesTextView.setText("총 칼로리: " + totalCalories + "cal");
    }


    // byte[]를 Bitmap으로 변환하는 메서드 (필요하다면 추가 구현)
    private Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        // ...
        return null;
    }
}
