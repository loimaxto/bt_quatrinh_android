package com.example.bt2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.model.Customer;
import com.example.bt2.model.CustomerManager;

import java.util.List;

public class login_bt2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gán layout activity_main.xml cho MainActivity
        setContentView(R.layout.login_bt2);
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);


        //taọ sự kiện cho nút chuyển mật khẩu
        TextView change_pass_btn = findViewById(R.id.change_pass_btn);
        change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change_pass_layout = new Intent(login_bt2.this,change_password_bt2.class);
                startActivity(change_pass_layout);
            }
        });

        CustomerManager customerManager = new CustomerManager(this);

        //Tạo sự kiện cho nút login
        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.username_txt)).getText().toString();
                String password = ((EditText) findViewById(R.id.password_txt)).getText().toString();
                //khởi tạo giá trị mặc định cho username và password nếu không có trong database
                if (!data.contains("Username") || !data.contains("password")) {
                    SharedPreferences.Editor editor = data.edit();
                    editor.putString("Username", "AnhHuy");
                    editor.putString("password", "0123456789");
                    editor.apply();
                }


                if(username.length() == 0 || password.length() == 0){
                    Toast.makeText(login_bt2.this, "Password or username can't be blank!", Toast.LENGTH_LONG).show();
                }
                else if(!username.equals(data.getString("Username",null))){
                    Toast.makeText(login_bt2.this, "Username isn't correct!", Toast.LENGTH_LONG).show();
                }
                else if(password.length() != 10){
                    Toast.makeText(login_bt2.this, "Password must be have 10 number!", Toast.LENGTH_LONG).show();
                }
                else if(!password.equals(data.getString("password",null))){
                    Toast.makeText(login_bt2.this, "Password isn't correct!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(login_bt2.this, "Login successful", Toast.LENGTH_LONG).show();
                    Intent input_point = new Intent(login_bt2.this, input_point_bt2.class);
                    startActivity(input_point);
                }


            }
        });

    }
}
