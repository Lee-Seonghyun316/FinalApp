package com.example.finalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MealInputFragment extends Fragment {

    private Spinner spinnerPlace, spinnerType;
    private ImageView imageViewFood;
    private EditText editTextFoodName, editTextDate, editTextCost, editTextCalories, editTextEvaluationText;
    private RatingBar ratingBar;
    private Button btnSelectImage, btnSaveMeal;

    private MealDatabaseHelper databaseHelper;
    private static final int REQUEST_IMAGE_PICK = 1;
    private byte[] selectedImageBlob;

    private DatePicker datePicker;


    public MealInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // UI 요소 초기화
        spinnerPlace = view.findViewById(R.id.spinnerPlace);
        spinnerType = view.findViewById(R.id.spinnerType);
        imageViewFood = view.findViewById(R.id.imageViewFood);
        editTextFoodName = view.findViewById(R.id.editTextFoodName);
        datePicker = view.findViewById(R.id.datePicker);
        editTextCost = view.findViewById(R.id.editTextCost);
        editTextCalories = view.findViewById(R.id.editTextCalories);
        editTextEvaluationText = view.findViewById(R.id.editTextEvaluationText);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSaveMeal = view.findViewById(R.id.btnSaveMeal);

        // DB Helper 초기화
        databaseHelper = new MealDatabaseHelper(requireContext());

        // 장소 Spinner 설정
        ArrayAdapter<CharSequence> placeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.place_array,
                android.R.layout.simple_spinner_item
        );
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlace.setAdapter(placeAdapter);

        // 타입 Spinner 설정
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.type_array,
                android.R.layout.simple_spinner_item
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // 이미지 선택 버튼 클릭 이벤트
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 선택 로직 추가
                openGallery();
            }
        });

        // 저장 버튼 클릭 이벤트
        btnSaveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 저장 로직 추가
                saveMeal();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            // 갤러리에서 선택한 이미지를 처리
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewFood.setImageBitmap(bitmap);

                // 이미지를 byte 배열로 변환하여 저장
                selectedImageBlob = convertBitmapToByteArray(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void saveMeal() {
        String place = spinnerPlace.getSelectedItem().toString();
        byte[] imageBlob = getImageBlob();
        String foodName = editTextFoodName.getText().toString();
        Date date = getSelectedDate();
        int cost = Integer.parseInt(editTextCost.getText().toString());
        int calories = Integer.parseInt(editTextCalories.getText().toString());
        String type = spinnerType.getSelectedItem().toString();
        String evaluationText = getEvaluationText();

        Meal meal = new Meal(place, imageBlob, foodName, evaluationText, date, cost, calories, type);

        // DB에 저장
        try {
            long rowId = databaseHelper.insertMeal(meal);

            if (rowId != -1) {
                showToast("식사가 저장되었습니다. " + (rowId + 1) + "번째 식사");
            } else {
                showToast("식사 저장에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("식사 저장 중 오류가 발생했습니다.");

            // 로그에 에러 메시지 출력
            Log.e("MealInputFragment", "Error saving meal", e);
        }
    }

    private Date getSelectedDate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    private byte[] getImageBlob() {
        return selectedImageBlob;
    }

    private String getEvaluationText() {
        float rating = ratingBar.getRating();
        String evaluationText = editTextEvaluationText.getText().toString();
        return String.format(Locale.getDefault(), "%.1f점: %s", rating, evaluationText);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}