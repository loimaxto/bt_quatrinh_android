package com.example.bt2;

import static java.lang.System.*;
import static java.sql.DriverManager.println;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class change_password_bt2 extends AppCompatActivity {

    public String default_username = "AnhHuy";
    public String default_password = "0123456789";
    private SharedPreferences data; //nơi để lưu trữ mật khẩu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_bt2);

        data = getSharedPreferences("data",MODE_PRIVATE); //tạo tên SharedPreferences


            // Viết xử lý cho nút enter
        Button enter_btn = findViewById(R.id.enter_btn);  // Khai báo Button
        enter_btn.setOnClickListener(new View.OnClickListener() {  // Đặt OnClickListener
            @Override
            public void onClick(View view) {
                String old_pass = ((EditText) findViewById(R.id.curr_pass_txt)).getText().toString();
                String new_pass = ((EditText) findViewById(R.id.new_pass_txt)).getText().toString();
                String confirm_pass = ((EditText) findViewById(R.id.confirm_pass_txt)).getText().toString();

                if(check_renew_pass(old_pass,new_pass,confirm_pass)){
                    changePassword(new_pass);
                    Toast.makeText(change_password_bt2.this, "Password is saved", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    if(!old_pass.equals(getPassword())){
                        Toast.makeText(change_password_bt2.this, "Old password is not correct", Toast.LENGTH_LONG).show();
                    }

                    else if(new_pass.length() != 10){
                        Toast.makeText(change_password_bt2.this, "New password must be have 10 numbers", Toast.LENGTH_LONG).show();
                    }
                    else if(new_pass.equals(old_pass)){
                        Toast.makeText(change_password_bt2.this, "The old password can not match the new password.", Toast.LENGTH_LONG).show();
                    }
                    else if(!new_pass.equals(confirm_pass)){
                        Toast.makeText(change_password_bt2.this, "The confirm password must match the password.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //taọ sự kiện cho nút chuyển go back
        TextView go_back_btn = findViewById(R.id.go_back_btn);
        go_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



//các hàm phụ trợ
    public boolean check_renew_pass(String old_pass,String new_pass, String confirm_pass){
        if(old_pass.equals(getPassword()) && new_pass.equals(confirm_pass) && new_pass.length() == 10) return true;
        else return false;
    }

public void changePassword(String newPassword) {
    SharedPreferences.Editor editor = data.edit();
    editor.putString("password", newPassword);
    editor.apply(); // Lưu mật khẩu mới
}
    // Lấy mật khẩu (ưu tiên mật khẩu đã thay đổi nếu có)
    private String getPassword() {
        String password = data.getString("password", null);
        if (password == null || password.isEmpty()) {
            return default_password;
        } else {
            return password;
        }
    }
    private String getUsername() {
        String Username = data.getString("Username", null);
        if (Username == null || Username.isEmpty()) {
            return default_username;
        } else {
            return Username;
        }

    }
}

