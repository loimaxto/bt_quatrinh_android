package com.example.bt2.java;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.R;
import com.example.bt2.model.Customer;
import com.example.bt2.model.CustomerManager;
import com.example.bt2.provider.DatabaseManager;

import java.util.List;

public class danh_sach_kh_bt2 extends AppCompatActivity {

    private DatabaseManager dbManager;  // Quản lý cơ sở dữ liệu SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danh_sach_kh_bt2);

        displayCustomersFromSharedPreferences(); //lấy ra danh sách khách hàng


        // Sự kiện cho các nút chuyển trang
        Button input_btn = findViewById(R.id.input_btn);
        input_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent input_btn = new Intent(danh_sach_kh_bt2.this, input_point_bt2.class);
                startActivity(input_btn);  // Thêm startActivity để chuyển trang
            }
        });
        Button use_btn = findViewById(R.id.use_btn);
        use_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent use_point = new Intent(danh_sach_kh_bt2.this, use_point_bt2.class);
                startActivity(use_point);  // Thêm startActivity để chuyển trang
            }
        });
        Button list_btn = findViewById(R.id.list_btn);
        list_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                recreate();

            }
        });
    }

    private void displayCustomersFromSharedPreferences() {
        dbManager = new DatabaseManager(this);
        List<Customer> customers = dbManager.getAllCustomers(); // Lấy danh sách khách hàng

        LinearLayout parentLayout = findViewById(R.id.parent_layout); // Parent layout ID

        for (Customer customer : customers) {
            // Tạo LinearLayout ngang cho từng khách hàng
            LinearLayout customerLayout = new LinearLayout(this);
            customerLayout.setOrientation(LinearLayout.HORIZONTAL);
            customerLayout.setPadding(0, 16, 0, 16); // Padding giữa mỗi dòng khách hàng
            customerLayout.setBackgroundColor(Color.parseColor("#000000")); // Màu nền đen

            // Cột bên trái chứa thông tin số điện thoại, ngày tạo và ghi chú
            LinearLayout layout_left = new LinearLayout(this);
            layout_left.setOrientation(LinearLayout.VERTICAL);
            layout_left.setPadding(16, 0, 16, 0); // Thêm padding cho cột bên trái
            LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f); // Sử dụng weight để điều chỉnh
            layout_left.setLayoutParams(leftParams);

            TextView phoneTextView = new TextView(this);
            phoneTextView.setText(customer.getPhoneNumber());
            phoneTextView.setTextColor(Color.parseColor("#FFFF66"));
            phoneTextView.setTextSize(24);

            TextView dateCreateView = new TextView(this);
            dateCreateView.setText(customer.getCreatedDate());
            dateCreateView.setTextColor(Color.parseColor("#BBBBBB"));
            dateCreateView.setTextSize(18);

            TextView noteTextView = new TextView(this);
            noteTextView.setText(customer.getNote());
            noteTextView.setTextColor(Color.parseColor("#BBBBBB"));
            noteTextView.setTextSize(18);

            layout_left.addView(phoneTextView);
            layout_left.addView(dateCreateView);
            layout_left.addView(noteTextView);

            // Cột bên phải chứa điểm và ngày cập nhật
            LinearLayout layout_right = new LinearLayout(this);
            layout_right.setOrientation(LinearLayout.VERTICAL);
            layout_right.setPadding(16, 0, 16, 0); // Padding bên phải
            LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f); // Sử dụng weight để căn chỉnh
            layout_right.setLayoutParams(rightParams);
            layout_right.setGravity(Gravity.END); // Căn phải tất cả nội dung

            TextView pointTextView = new TextView(this);
            pointTextView.setText(String.valueOf(customer.getPoint()));
            pointTextView.setTextColor(Color.parseColor("#FFFF66"));
            pointTextView.setTextSize(24);
            pointTextView.setGravity(Gravity.END); // Căn phải text

            TextView dateUpdateView = new TextView(this);
            dateUpdateView.setText(customer.getUpdatedDate());
            dateUpdateView.setTextColor(Color.parseColor("#BBBBBB"));
            dateUpdateView.setTextSize(18);
            dateUpdateView.setGravity(Gravity.END); // Căn phải text

            layout_right.addView(pointTextView);
            layout_right.addView(dateUpdateView);

            // Thêm cột trái và phải vào layout khách hàng
            customerLayout.addView(layout_left);
            customerLayout.addView(layout_right);

            // Tạo nút xóa và lưu số điện thoại vào thuộc tính `tag`
            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setImageResource(R.drawable.baseline_delete_24);
            deleteButton.setBackgroundColor(Color.TRANSPARENT); // Nền trong suốt cho nút xóa
            deleteButton.setColorFilter(Color.parseColor("#FF0000")); // Màu đỏ cho icon xóa

            // Lưu số điện thoại vào `tag` của nút xóa
            deleteButton.setTag(customer.getPhoneNumber());

            // Sự kiện khi bấm nút xóa
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumberToDelete = (String) v.getTag(); // Lấy số điện thoại từ `tag`

                    // Xóa khách hàng khỏi SharedPreferences
                    dbManager.deleteCustomer(phoneNumberToDelete);

                    // Cập nhật lại giao diện
                    parentLayout.removeView(customerLayout);
                    Toast.makeText(getApplicationContext(), "Deleted customer with phone: " + phoneNumberToDelete, Toast.LENGTH_SHORT).show();
                }
            });

            // Thêm nút xóa vào layout của khách hàng
            customerLayout.addView(deleteButton);

            // Thêm layout của khách hàng vào parent layout
            parentLayout.addView(customerLayout);
        }
    }



}

