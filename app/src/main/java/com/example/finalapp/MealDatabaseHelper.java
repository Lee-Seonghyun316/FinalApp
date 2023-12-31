package com.example.finalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class MealDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meal_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "meals";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_IMAGE_BLOB = "imageBlob";
    private static final String COLUMN_MENU_NAME = "menuName";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_TYPE = "type";
    private long id;

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PLACE + " TEXT," +
                    COLUMN_IMAGE_BLOB + " BLOB," +
                    COLUMN_MENU_NAME + " TEXT," +
                    COLUMN_RATING + " TEXT," +
                    COLUMN_TIME + " DATETIME," +
                    COLUMN_COST + " INTEGER," +
                    COLUMN_CALORIES + " INTEGER," +
                    COLUMN_TYPE + " TEXT);";

    public MealDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // insertMeal 메서드 수정
    public long insertMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = -1;

        try {
            // 트랜잭션 시작
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(COLUMN_PLACE, meal.getPlace());
            values.put(COLUMN_IMAGE_BLOB, meal.getImageBlob());
            values.put(COLUMN_MENU_NAME, meal.getMenuName());
            values.put(COLUMN_RATING, meal.getRating());
            values.put(COLUMN_TIME, meal.getTime().getTime());
            values.put(COLUMN_COST, meal.getCost());
            values.put(COLUMN_CALORIES, meal.getCalories());
            values.put(COLUMN_TYPE, meal.getType());

            rowId = db.insert(TABLE_NAME, null, values);

            // 트랜잭션 성공 시 커밋
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 트랜잭션 종료
            db.endTransaction();
            // 데이터베이스 연결 종료
            db.close();
        }

        return rowId;
    }

    // MealDatabaseHelper.java

// ...

    // 모든 식사 데이터 가져오기
    public ArrayList<Meal> getAllMeals() {
        ArrayList<Meal> mealList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                    String place = cursor.getString(cursor.getColumnIndex(COLUMN_PLACE));
                    byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_BLOB));
                    String menuName = cursor.getString(cursor.getColumnIndex(COLUMN_MENU_NAME));
                    String rating = cursor.getString(cursor.getColumnIndex(COLUMN_RATING));
                    long timeMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
                    int cost = cursor.getInt(cursor.getColumnIndex(COLUMN_COST));
                    int calories = cursor.getInt(cursor.getColumnIndex(COLUMN_CALORIES));
                    String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

                    Meal meal = new Meal(place, imageBlob, menuName, rating, new Date(timeMillis), cost, calories, type);
                    mealList.add(meal);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 로그에 에러 메시지 출력
            Log.e("getAllMeals", "Error retrieving meals", e);
        } finally {
            cursor.close();
        }

        return mealList;
    }

    // ...

    public int getTotalCaloriesForLastMonth() {
        int totalCalories = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        // Calculate the date one month ago from the current date
        long oneMonthAgoMillis = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

        // Define the columns you want to retrieve
        String[] columns = {COLUMN_CALORIES};

        // Specify the selection criteria
        String selection = COLUMN_TIME + " >= ?";
        String[] selectionArgs = {String.valueOf(oneMonthAgoMillis)};

        // Query the database
        Cursor cursor = db.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Process the cursor and calculate total calories
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int calories = cursor.getInt(cursor.getColumnIndex(COLUMN_CALORIES));
                totalCalories += calories;
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Close the database connection
        db.close();

        return totalCalories;
    }

    public ArrayList<MealTypeAnalysis> getCostAnalysisForLastMonth() {
        ArrayList<MealTypeAnalysis> analysisList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Calculate the date one month ago from the current date
        long oneMonthAgoMillis = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

        // Define the columns you want to retrieve
        String[] columns = {COLUMN_COST, COLUMN_TYPE};

        // Specify the selection criteria
        String selection = COLUMN_TIME + " >= ?";
        String[] selectionArgs = {String.valueOf(oneMonthAgoMillis)};

        // Query the database
        Cursor cursor = db.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Process the cursor and populate the analysisList
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int cost = cursor.getInt(cursor.getColumnIndex(COLUMN_COST));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

                // Update the corresponding analysis or create a new one
                MealTypeAnalysis analysis = findAnalysisByType(analysisList, type);
                if (analysis != null) {
                    analysis.addToTotalCost(cost);
                } else {
                    analysisList.add(new MealTypeAnalysis(type, cost));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Close the database connection
        db.close();

        return analysisList;
    }

    private MealTypeAnalysis findAnalysisByType(ArrayList<MealTypeAnalysis> analysisList, String type) {
        for (MealTypeAnalysis analysis : analysisList) {
            if (analysis.getType().equals(type)) {
                return analysis;
            }
        }
        return null;
    }
    // ...

    public ArrayList<Meal> getMealsSinceDate(Date startDate) {
        ArrayList<Meal> mealList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                COLUMN_ID,
                COLUMN_PLACE,
                COLUMN_IMAGE_BLOB,
                COLUMN_MENU_NAME,
                COLUMN_RATING,
                COLUMN_TIME,
                COLUMN_COST,
                COLUMN_CALORIES,
                COLUMN_TYPE
        };

        // Specify the selection criteria
        String selection = COLUMN_TIME + " >= ?";
        String[] selectionArgs = {String.valueOf(startDate.getTime())};

        // Query the database
        Cursor cursor = db.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Process the cursor and populate the mealList
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                String place = cursor.getString(cursor.getColumnIndex(COLUMN_PLACE));
                byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_BLOB));
                String menuName = cursor.getString(cursor.getColumnIndex(COLUMN_MENU_NAME));
                String rating = cursor.getString(cursor.getColumnIndex(COLUMN_RATING));
                long timeMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME));
                int cost = cursor.getInt(cursor.getColumnIndex(COLUMN_COST));
                int calories = cursor.getInt(cursor.getColumnIndex(COLUMN_CALORIES));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

                Meal meal = new Meal(place, imageBlob, menuName, rating, new Date(timeMillis), cost, calories, type);
                meal.setId(id);
                mealList.add(meal);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Close the database connection
        db.close();

        return mealList;
    }


// ...


// ...


}

