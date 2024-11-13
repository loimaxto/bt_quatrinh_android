package com.example.bt2.java;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.R;
import com.example.bt2.provider.DatabaseManager;

public class login_bt2 extends AppCompatActivity {

    private String default_username = "AnhHuy";  // Username mặc định
    private String default_password = "0123456789";  // Mật khẩu mặc định
    private DatabaseManager dbManager;  // Quản lý cơ sở dữ liệu SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_bt2);

        dbManager = new DatabaseManager(this);  // Khởi tạo DatabaseManager

        // Tạo sự kiện cho nút chuyển mật khẩu
        TextView change_pass_btn = findViewById(R.id.change_pass_btn);
        change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change_pass_layout = new Intent(login_bt2.this, change_password_bt2.class);
                startActivity(change_pass_layout);
            }
        });

        // Kiểm tra và khởi tạo giá trị mặc định nếu chưa có trong cơ sở dữ liệu
        dbManager.open();
        if (!customerExists(default_username)) {
            insertDefaultUser(default_username, default_password);
        }
//        dbManager.close();

        // Tạo sự kiện cho nút login
        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.username_txt)).getText().toString();
                String password = ((EditText) findViewById(R.id.password_txt)).getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login_bt2.this, "Username or password can't be blank!", Toast.LENGTH_LONG).show();
                } else if (!username.equals(getUsername())) {
                    Toast.makeText(login_bt2.this, "Username isn't correct!", Toast.LENGTH_LONG).show();
                } else if (password.length() != 10) {
                    Toast.makeText(login_bt2.this, "Password must have 10 digits!", Toast.LENGTH_LONG).show();
                } else if (!password.equals(getPassword(username))) {
                    Toast.makeText(login_bt2.this, "Password isn't correct!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(login_bt2.this, "Login successful", Toast.LENGTH_LONG).show();
                    Intent input_point = new Intent(login_bt2.this, input_point_bt2.class);
                    startActivity(input_point);
                }
            }
        });
    }

    // Kiểm tra xem người dùng có tồn tại trong cơ sở dữ liệu không
    private boolean customerExists(String username) {
        String password = dbManager.getPassword(username);  // Sử dụng DatabaseManager để lấy mật khẩu
        return password != null;  // Nếu trả về null, người dùng không tồn tại
    }

    // Chèn người dùng mặc định vào cơ sở dữ liệu
    private void insertDefaultUser(String username, String password) {
        dbManager.open();
        dbManager.insertUser(username, password);  // Chèn người dùng mặc định vào cơ sở dữ liệu

    }

    // Lấy username từ cơ sở dữ liệu (có thể trả về giá trị mặc định)
    private String getUsername() {
        return default_username;  // Trả về username mặc định
    }

    // Lấy mật khẩu từ cơ sở dữ liệu cho người dùng với username cụ thể
    private String getPassword(String username) {
        return dbManager.getPassword(username);  // Sử dụng DatabaseManager để lấy mật khẩu
    }
}
