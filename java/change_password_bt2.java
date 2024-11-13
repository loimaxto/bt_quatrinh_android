package com.example.bt2.java;

import android.content.ContentValues;
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

public class change_password_bt2 extends AppCompatActivity {

    private String default_username = "AnhHuy";  // Đây là username mặc định
    private String default_password = "0123456789";  // Đây là mật khẩu mặc định

    private DatabaseManager dbManager;  // Khai báo DatabaseManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_bt2);

        dbManager = new DatabaseManager(this);  // Khởi tạo DatabaseManager

        // Viết xử lý cho nút enter
        Button enter_btn = findViewById(R.id.enter_btn);
        enter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old_pass = ((EditText) findViewById(R.id.curr_pass_txt)).getText().toString();
                String new_pass = ((EditText) findViewById(R.id.new_pass_txt)).getText().toString();
                String confirm_pass = ((EditText) findViewById(R.id.confirm_pass_txt)).getText().toString();

                if (check_renew_pass(old_pass, new_pass, confirm_pass)) {
                    changePassword(new_pass);
                    Toast.makeText(change_password_bt2.this, "Password is saved", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    handleInvalidInput(old_pass, new_pass, confirm_pass);
                }
            }
        });

        // Tạo sự kiện cho nút chuyển go back
        TextView go_back_btn = findViewById(R.id.go_back_btn);
        go_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Hàm phụ trợ kiểm tra mật khẩu mới
    public boolean check_renew_pass(String old_pass, String new_pass, String confirm_pass) {
        return old_pass.equals(getPassword()) && new_pass.equals(confirm_pass) && new_pass.length() == 10;
    }

    // Thay đổi mật khẩu trong cơ sở dữ liệu SQLite
    public void changePassword(String newPassword) {
        dbManager.open();
        dbManager.updatePassword(default_username, newPassword);  // Cập nhật mật khẩu mới vào cơ sở dữ liệu
        dbManager.close();
    }

    // Lấy mật khẩu từ cơ sở dữ liệu SQLite
    private String getPassword() {
        dbManager.open();
        String password = dbManager.getPassword(default_username);  // Lấy mật khẩu từ cơ sở dữ liệu
        dbManager.close();
        return password != null ? password : default_password;  // Nếu không tìm thấy, trả về mật khẩu mặc định
    }

    // Xử lý các trường hợp nhập sai mật khẩu
    private void handleInvalidInput(String old_pass, String new_pass, String confirm_pass) {
        if (!old_pass.equals(getPassword())) {
            Toast.makeText(this, "Old password is not correct", Toast.LENGTH_LONG).show();
        } else if (new_pass.length() != 10) {
            Toast.makeText(this, "New password must have 10 numbers", Toast.LENGTH_LONG).show();
        } else if (new_pass.equals(old_pass)) {
            Toast.makeText(this, "The old password cannot match the new password.", Toast.LENGTH_LONG).show();
        } else if (!new_pass.equals(confirm_pass)) {
            Toast.makeText(this, "The confirm password must match the password.", Toast.LENGTH_LONG).show();
        }
    }
}
