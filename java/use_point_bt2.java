package com.example.bt2.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.R;
import com.example.bt2.model.Customer;
import com.example.bt2.model.CustomerManager;
import com.example.bt2.provider.DatabaseManager;

import java.util.List;

public class use_point_bt2 extends AppCompatActivity {

    private DatabaseManager dbManager;  // Quản lý cơ sở dữ liệu SQLite


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DatabaseManager(this);

        setContentView(R.layout.use_point_bt2);

        EditText phonenumber = findViewById(R.id.numberphope);
        TextView curr_point = findViewById(R.id.curr_point);
        EditText usedPointEditText = findViewById(R.id.used_point);
        EditText noteEditText = findViewById(R.id.note);


        // Lắng nghe sự thay đổi trong EditText
        phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String phoneNumber = charSequence.toString().trim();
                List<Customer> customers = dbManager.getAllCustomers();


                boolean exist = false;

                // Duyệt qua từng khách hàng
                for (Customer customer : customers) {
                    if(phoneNumber.equals(customer.getPhoneNumber())){
                        exist = true;
                        curr_point.setText(String.valueOf(customer.getPoint())); //ghi điểm hiện tại
                        break;
                    }
                }
                if(!exist){
                    curr_point.setText("0"); //ghi điểm hiện tại
                }
            }
        });

// Sự kiện cho nút save
        Button save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_note = noteEditText.getText().toString().trim();
                String newPointStr = usedPointEditText.getText().toString().trim();
                String phone_text = phonenumber.getText().toString().trim();

                // Check if phone number exists in the database
                List<Customer> customers = dbManager.getAllCustomers();
                boolean phoneExists = false;
                for (Customer customer : customers) {
                    if (phone_text.equals(customer.getPhoneNumber())) {
                        phoneExists = true;
                        break;
                    }
                }

                // Check if phone number is entered
                if (phone_text.isEmpty() || !phoneExists) {
                    Toast.makeText(use_point_bt2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;  // Stop if the phone number is empty
                }

                int newPoint = 0;

                // Parse newPoint if available
                if (!newPointStr.isEmpty()) {
                    try {
                        newPoint = Integer.parseInt(newPointStr);  // Convert to int
                    } catch (NumberFormatException e) {
                        Toast.makeText(use_point_bt2.this, "Invalid point value", Toast.LENGTH_SHORT).show();
                        return;  // Stop if the entered points are invalid
                    }
                }

                // Check if both note and points are empty
                if (new_note.isEmpty() && newPoint == 0) {
                    Toast.makeText(use_point_bt2.this, "New points and note are currently empty", Toast.LENGTH_SHORT).show();
                    return;  // Stop if no information to update
                }

                boolean isUpdated = false;  // Flag to check if anything was updated

                // Update points if available
                if (newPoint != 0) {
                    dbManager.minusPoint(use_point_bt2.this,phone_text, newPoint);  // Cập nhật điểm
                    isUpdated = true;  // Set flag to true if points are updated
                }

                // Update note if available
                if (!new_note.isEmpty()) {
                    dbManager.updateNote(phone_text, new_note);  // Cập nhật ghi chú
                    isUpdated = true;  // Set flag to true if note is updated
                }

                // Check if any update was performed
                if (isUpdated) {
                    Toast.makeText(use_point_bt2.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(use_point_bt2.this, "No changes were made", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Sự kiện cho nút saveAnext
        Button saveAnext = findViewById(R.id.saveAnext);
        saveAnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_note = noteEditText.getText().toString().trim();
                String newPointStr = usedPointEditText.getText().toString().trim();
                String phone_text = phonenumber.getText().toString().trim();

                // Check if phone number exists in the database
                List<Customer> customers = dbManager.getAllCustomers();
                boolean phoneExists = false;
                for (Customer customer : customers) {
                    if (phone_text.equals(customer.getPhoneNumber())) {
                        phoneExists = true;
                        break;
                    }
                }

                // Kiểm tra nếu số điện thoại không được nhập
                if (phone_text.isEmpty() || !phoneExists) {
                    Toast.makeText(use_point_bt2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;  // Dừng lại nếu số điện thoại rỗng
                }

                int newPoint = 0;

                // Chuyển đổi giá trị newPoint nếu có
                if (!newPointStr.isEmpty()) {
                    try {
                        newPoint = Integer.parseInt(newPointStr);  // Chuyển thành số nguyên
                    } catch (NumberFormatException e) {
                        Toast.makeText(use_point_bt2.this, "Invalid point value", Toast.LENGTH_SHORT).show();
                        return;  // Dừng lại nếu giá trị điểm không hợp lệ
                    }
                }

                // Kiểm tra nếu cả ghi chú và điểm đều trống
                if (new_note.isEmpty() && newPoint == 0) {
                    Toast.makeText(use_point_bt2.this, "New points and note are currently empty", Toast.LENGTH_SHORT).show();
                    return;  // Dừng lại nếu không có thông tin để cập nhật
                }

                boolean isUpdated = false;  // Biến để kiểm tra nếu có cập nhật nào

                // Cập nhật điểm nếu có
                if (newPoint != 0) {

                    dbManager.minusPoint(use_point_bt2.this,phone_text, newPoint);  // Cập nhật điểm
                    isUpdated = true;  // Đánh dấu là đã cập nhật
                }

                // Cập nhật ghi chú nếu có
                if (!new_note.isEmpty()) {
                    dbManager.updateNote(phone_text, new_note);  // Cập nhật ghi chú
                    isUpdated = true;  // Đánh dấu là đã cập nhật
                }

                // Kiểm tra nếu có thay đổi nào được thực hiện
                if (isUpdated) {
                    Toast.makeText(use_point_bt2.this, "Successfully updated", Toast.LENGTH_SHORT).show();

                    // Chuyển đến màn hình tiếp theo
                    Intent use_point = new Intent(use_point_bt2.this, danh_sach_kh_bt2.class);
                    startActivity(use_point);
                } else {
                    Toast.makeText(use_point_bt2.this, "No changes were made", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sự kiện cho các nút chuyển trang
        Button input_btn = findViewById(R.id.input_btn);
        input_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent input_point = new Intent(use_point_bt2.this, input_point_bt2.class);
                startActivity(input_point);  // Thêm startActivity để chuyển trang
            }
        });

        Button use_btn = findViewById(R.id.use_btn);
        use_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        Button list_btn = findViewById(R.id.list_btn);
        list_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent use_point = new Intent(use_point_bt2.this, danh_sach_kh_bt2.class);
                startActivity(use_point);  // Thêm startActivity để chuyển trang
            }
        });
    }
    }

