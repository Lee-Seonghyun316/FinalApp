package com.example.finalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

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

    // 모든 식사 데이터 가져오기
    public Cursor getAllMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}

