package com.example.bt_quatrinh_android.BT3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper  extends SQLiteOpenHelper  {

    private static final String DATABASE_NAME = "photo_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PHOTOS = "photos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PHOTO = "photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_PHOTO + " BLOB" + ")";
        db.execSQL(CREATE_PHOTOS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }
    // luu anh
    public boolean saveImage(Bitmap image, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(COLUMN_DATE,date);
        value.put(COLUMN_PHOTO,getBytes(image));

        long result = db.insert(TABLE_PHOTOS,null,value);
        db.close();
        return result != -1;
    }


    // Lấy tất cả ảnh được phân theo ngày
    public List<Image> getAllImagesGroupedByDate() {
        List<Image> images = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PHOTOS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)); //id
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)); //date
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length); // anh
                images.add(new Image(id, date, image)); //item
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return images;
    }
    public boolean deleteImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PHOTOS,COLUMN_ID + "= ?", new String[]{String.valueOf(image.getId())});
        db.close();
        return result > 0;
    }

    public void deleteAllImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTOS, null, null);
        db.close();
    }
    // Phương thức chuyển đổi Bitmap thành byte[] để lưu trữ trong cơ sở dữ liệu
    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Nén ảnh dưới dạng JPEG để giảm dung lượng
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); // 90 là chất lượng nén
        return stream.toByteArray();
    }
}
