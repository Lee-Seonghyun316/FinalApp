package com.example.finalapp;
// MealDetailActivity.java

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MealDetailActivity extends AppCompatActivity {

    // 각 항목에 대한 TextView 선언
    private TextView textViewPlace;
    private ImageView imageViewMeal;
    private TextView textViewMenuName;
    private TextView textViewRating;
    private TextView textViewTime;
    private TextView textViewCost;
    private TextView textViewCalories;
    private TextView textViewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        // 각 항목에 대한 TextView 초기화
        textViewPlace = findViewById(R.id.textViewPlace);
        imageViewMeal = findViewById(R.id.imageViewMeal);
        textViewMenuName = findViewById(R.id.textViewMenuName);
        textViewRating = findViewById(R.id.textViewRating);
        textViewTime = findViewById(R.id.textViewTime);
        textViewCost = findViewById(R.id.textViewCost);
        textViewCalories = findViewById(R.id.textViewCalories);
        textViewType = findViewById(R.id.textViewType);

        // Intent에서 Meal 객체 가져오기
        Intent intent = getIntent();
        if (intent.hasExtra("meal")) {
            Meal meal = (Meal) intent.getSerializableExtra("meal");

            // 각 TextView에 데이터 설정
            textViewPlace.setText("- " + meal.getPlace());

            // 이미지 설정
            byte[] imageBlob = meal.getImageBlob();
            if (imageBlob != null && imageBlob.length > 0) {
                imageViewMeal.setImageBitmap(BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length));
            } else {
                // 이미지가 없는 경우 기본 이미지 설정
                imageViewMeal.setImageResource(R.drawable.default_image);
            }

            textViewMenuName.setText("- " + meal.getMenuName());
            textViewRating.setText("- " + meal.getRating());
            textViewTime.setText("- " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(meal.getTime()));
            textViewCost.setText("- " + meal.getCost() + "₩");
            textViewCalories.setText("- " + meal.getCalories());
            textViewType.setText("- " + meal.getType());
        }
    }
}
