package com.example.finalapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MealListAdapter extends ArrayAdapter<Meal> {

    private final Context context;
    private final ArrayList<Meal> mealList;

    public MealListAdapter(Context context, ArrayList<Meal> mealList) {
        super(context, R.layout.meal_list_item, mealList);
        this.context = context;
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

        // 이미지 설정 (byte[]에서 이미지로 변환하는 로직이 필요함)
        // imageView.setImageBitmap(convertByteArrayToBitmap(meal.getImageBlob()));

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

        return rowView;
    }

// ...


    // byte[]를 Bitmap으로 변환하는 메서드 (필요하다면 추가 구현)
    private Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        // ...
        return null;
    }
}
