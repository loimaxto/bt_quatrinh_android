package com.example.bt2.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;  // Tăng phiên bản cơ sở dữ liệu để thực hiện việc nâng cấp

    // Các bảng và cột
    public static final String TABLE_USER = "user";
    public static final String TABLE_CUSTOMER = "customer";



    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_CREATED_DATE = "createdDate";
    public static final String COLUMN_UPDATED_POINTS_DATE = "updatedPointsDate";
    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_NOTE = "note";

    // Câu lệnh SQL tạo bảng
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL);";

    private static final String CREATE_CUSTOMER_TABLE =
            "CREATE TABLE " + TABLE_CUSTOMER + " (" +
                    COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                    COLUMN_CREATED_DATE + " TEXT NOT NULL, " +
                    COLUMN_UPDATED_POINTS_DATE + " TEXT NOT NULL, " +
                    COLUMN_POINTS + " INTEGER NOT NULL, " +
                    COLUMN_NOTE + " TEXT NOT NULL);";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo các bảng khi cơ sở dữ liệu được tạo lần đầu
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Khi nâng cấp cơ sở dữ liệu, chúng ta sẽ xóa các bảng cũ và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        onCreate(db);
    }
}
