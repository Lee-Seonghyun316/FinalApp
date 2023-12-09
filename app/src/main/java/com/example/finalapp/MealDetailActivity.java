package com.example.finalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MealDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        // Intent에서 Meal 객체 가져오기
        Intent intent = getIntent();
        if (intent.hasExtra("meal")) {
            Meal meal = (Meal) intent.getSerializableExtra("meal");
            if (meal != null) {
                // 뷰 초기화
                ImageView imageView = findViewById(R.id.detailImageView);
                TextView menuNameTextView = findViewById(R.id.detailMenuNameTextView);
                TextView dateTextView = findViewById(R.id.detailDateTextView);
                // 나머지 뷰 초기화 (필요한 만큼 추가)

                // 이미지 설정 (byte[]에서 이미지로 변환하는 로직이 필요함)
                // imageView.setImageBitmap(convertByteArrayToBitmap(meal.getImageBlob()));

                // 나머지 데이터 설정
                menuNameTextView.setText(meal.getMenuName());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String formattedDate = sdf.format(meal.getTime());
                dateTextView.setText(formattedDate);
                // 나머지 데이터 설정 (필요한 만큼 추가)
            }
        }
    }

    // byte[]를 Bitmap으로 변환하는 메서드 (필요하다면 추가 구현)
    private Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        // ...
        return null;
    }
}

