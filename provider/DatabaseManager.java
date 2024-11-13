package com.example.bt2.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.example.bt2.model.Customer;
import com.example.bt2.provider.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {

    // Đặt authority của bạn tại đây (tên gói thường dùng làm authority)
    public static final String AUTHORITY = "com.example.bt2.provider";

    // Đường dẫn chung cho tất cả các dữ liệu khách hàng
    public static final String PATH_CUSTOMERS = "customers";

    // Tạo CONTENT_URI bằng cách sử dụng authority và đường dẫn
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_CUSTOMERS);
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        open();
    }

    // Mở cơ sở dữ liệu
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    // Đóng cơ sở dữ liệu
    public void close() {
        dbHelper.close();
    }


    // Chèn dữ liệu vào bảng user (cập nhật với username và password)
    public long insertUser(String username, String password) {
        ContentValues values = new ContentValues();

        // Chèn thông tin vào bảng user
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        // Chèn và trả về ID của bản ghi vừa được chèn
        return db.insert(DatabaseHelper.TABLE_USER, null, values);
    }


    public String getPassword(String username) {
        String password = null;  // Khởi tạo một biến tạm thời
        Cursor cursor = null;    // Khởi tạo con trỏ Cursor
        try {
            cursor = db.query(DatabaseHelper.TABLE_USER,
                    new String[]{DatabaseHelper.COLUMN_PASSWORD},
                    DatabaseHelper.COLUMN_USERNAME + "=?",
                    new String[]{username}, null, null, null);

            // Kiểm tra Cursor trước khi truy cập
            if (cursor != null && cursor.moveToFirst()) {
                password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
            }
        } finally {
            // Đảm bảo Cursor được đóng lại sau khi sử dụng
            if (cursor != null) {
                cursor.close();
            }
        }
        return password;  // Trả về password hoặc null nếu không tìm thấy
    }


    // Cập nhật mật khẩu cho user
    public void updatePassword(String username, String newPassword) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PASSWORD, newPassword);
        db.update(DatabaseHelper.TABLE_USER, values,
                DatabaseHelper.COLUMN_USERNAME + "=?",
                new String[]{username});
    }

    // Hàm lấy danh sách số điện thoại của tất cả khách hàng
    public List<String> getAllCustomerPhoneNumbers() {
        List<String> phoneNumbers = new ArrayList<>();

        // Truy vấn toàn bộ bảng CUSTOMER, chỉ lấy cột số điện thoại
        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMER,
                new String[]{DatabaseHelper.COLUMN_PHONE_NUMBER},  // Chỉ lấy cột số điện thoại
                null,  // Không có điều kiện lọc
                null,  // Không cần giá trị thay thế cho điều kiện lọc
                null,  // Không nhóm kết quả
                null,  // Không có điều kiện HAVING
                null); // Không sắp xếp kết quả

        // Duyệt qua kết quả và thêm số điện thoại vào danh sách
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE_NUMBER));
                phoneNumbers.add(phone);
            }
            cursor.close();  // Đóng cursor sau khi sử dụng
        }

        return phoneNumbers;  // Trả về danh sách số điện thoại
    }


    // Hàm chèn một khách hàng mới vào bảng Customer
    public long insertCustomer(Customer customer) {
        // Mở cơ sở dữ liệu để ghi
        db = dbHelper.getWritableDatabase();

        // Tạo ContentValues để chứa các cặp giá trị cột và dữ liệu
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, customer.getPhoneNumber());
        values.put(DatabaseHelper.COLUMN_POINTS, customer.getPoint());
        values.put(DatabaseHelper.COLUMN_CREATED_DATE, customer.getCreatedDate());
        values.put(DatabaseHelper.COLUMN_UPDATED_POINTS_DATE, customer.getUpdatedDate());
        values.put(DatabaseHelper.COLUMN_NOTE, customer.getNote());

        // Chèn dữ liệu vào bảng Customer
        long result = db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values);


        // Trả về ID của hàng vừa chèn, hoặc -1 nếu lỗi xảy ra
        return result;
    }


    // Hàm lấy danh sách tất cả khách hàng từ bảng Customer
    public List<Customer> getAllCustomers() {
        if (db == null) {
            throw new IllegalStateException("Database is not open.");
        }

        List<Customer> customerList = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMER,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE_NUMBER));
                int points = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINTS));
                String createdDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE));
                String updatedDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_POINTS_DATE));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE));

                Customer customer = new Customer(phoneNumber, createdDate, updatedDate, points, note);
                customerList.add(customer);
            }
            cursor.close();
        }

        return customerList;
    }



    // Hàm cộng điểm cho khách hàng dựa trên số điện thoại
    public void plusPoint(String phoneNumber, int additionalPoints) {
        // Tìm khách hàng dựa trên số điện thoại
        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMER,
                new String[]{DatabaseHelper.COLUMN_POINTS},  // Lấy cột điểm của khách hàng
                DatabaseHelper.COLUMN_PHONE_NUMBER + "=?",
                new String[]{phoneNumber}, null, null, null);

        // Kiểm tra nếu khách hàng tồn tại
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy điểm hiện tại của khách hàng
            int currentPoints = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINTS));

            // Cập nhật điểm mới
            int newPoints = currentPoints + additionalPoints;

            // Tạo ContentValues để cập nhật điểm mới
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_POINTS, newPoints);
            values.put(DatabaseHelper.COLUMN_UPDATED_POINTS_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // Cập nhật thời gian cập nhật điểm

            // Cập nhật lại bảng khách hàng
            db.update(DatabaseHelper.TABLE_CUSTOMER, values,
                    DatabaseHelper.COLUMN_PHONE_NUMBER + "=?",
                    new String[]{phoneNumber});
        }

        // Đảm bảo đóng cursor sau khi sử dụng
        if (cursor != null) {
            cursor.close();
        }
    }

    public void minusPoint(Context context, String phoneNumber, int minusPoints) {
        // Tìm khách hàng dựa trên số điện thoại
        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMER,
                new String[]{DatabaseHelper.COLUMN_POINTS},  // Lấy cột điểm của khách hàng
                DatabaseHelper.COLUMN_PHONE_NUMBER + "=?",
                new String[]{phoneNumber}, null, null, null);

        // Kiểm tra nếu khách hàng tồn tại
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy điểm hiện tại của khách hàng
            int currentPoints = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINTS));

            // Kiểm tra xem số điểm trừ có hợp lệ hay không (không trừ điểm nếu điểm hiện tại nhỏ hơn điểm muốn trừ)
            if (currentPoints >= minusPoints) {
                // Cập nhật điểm mới
                int newPoints = currentPoints - minusPoints;

                // Tạo ContentValues để cập nhật điểm mới
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_POINTS, newPoints);
                values.put(DatabaseHelper.COLUMN_UPDATED_POINTS_DATE,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // Cập nhật thời gian cập nhật điểm

                // Cập nhật lại bảng khách hàng
                int rowsUpdated = db.update(DatabaseHelper.TABLE_CUSTOMER, values,
                        DatabaseHelper.COLUMN_PHONE_NUMBER + "=?",
                        new String[]{phoneNumber});

            } else {
                // Nếu điểm hiện tại không đủ để trừ, thông báo lỗi
                Toast.makeText(context, "Insufficient points", Toast.LENGTH_SHORT).show();
            }
        }
        // Đảm bảo đóng cursor sau khi sử dụng
        if (cursor != null) {
            cursor.close();
        }
    }



    // Hàm cập nhật ghi chú (note) cho khách hàng dựa trên số điện thoại
    public void updateNote(String phoneNumber, String newNote) {
        // Tạo ContentValues để chứa giá trị cột và ghi chú mới
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE, newNote); // Cập nhật giá trị ghi chú mới
        values.put(DatabaseHelper.COLUMN_UPDATED_POINTS_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // Cập nhật thời gian thay đổi ghi chú

        // Cập nhật ghi chú cho khách hàng dựa trên số điện thoại
        int rowsAffected = db.update(DatabaseHelper.TABLE_CUSTOMER, values,
                DatabaseHelper.COLUMN_PHONE_NUMBER + "=?",
                new String[]{phoneNumber});
    }


    // Hàm xóa khách hàng dựa trên số điện thoại
    public void deleteCustomer(String phoneNumber) {
        // Xóa khách hàng khỏi bảng Customer dựa trên số điện thoại
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_CUSTOMER,
                DatabaseHelper.COLUMN_PHONE_NUMBER + "=?",
                new String[]{phoneNumber});


    }


}


