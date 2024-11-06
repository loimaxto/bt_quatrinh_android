package com.example.bt2;

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

import com.example.bt2.model.Customer;
import com.example.bt2.model.CustomerManager;

import java.util.List;

public class use_point_bt2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_point_bt2);

        EditText phonenumber = findViewById(R.id.numberphope);
        TextView curr_point = findViewById(R.id.curr_point);
        EditText usedPointEditText = findViewById(R.id.used_point);
        EditText noteEditText = findViewById(R.id.note);

        CustomerManager customerManager = new CustomerManager(this);

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
                List<Customer> customers = customerManager.getCustomers();

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

                // Kiểm tra nếu không có số điện thoại được nhập
                if (phone_text.isEmpty()) {
                    Toast.makeText(use_point_bt2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;  // Dừng lại nếu số điện thoại trống
                }

                int newPoint = 0;

                // Kiểm tra nếu trường điểm mới (newPoint) có giá trị và chuyển nó thành số nguyên
                if (!newPointStr.isEmpty()) {
                    try {
                        newPoint = Integer.parseInt(newPointStr);  // Chuyển thành int
                    } catch (NumberFormatException e) {
                        Toast.makeText(use_point_bt2.this, "Invalid point value", Toast.LENGTH_SHORT).show();
                        return;  // Dừng nếu điểm nhập không hợp lệ
                    }
                }

                // Kiểm tra nếu cả điểm mới và ghi chú đều trống
                if (new_note.isEmpty() && newPoint == 0) {
                    Toast.makeText(use_point_bt2.this, "New points and note are currently empty", Toast.LENGTH_SHORT).show();
                    return;  // Dừng nếu không có thông tin để cập nhật
                }

                // Cập nhật điểm mới
                if (newPoint != 0) {
                    customerManager.use(newPoint, phone_text);  // tru điểm mới
                }

                // Cập nhật ghi chú mới nếu có
                if (!new_note.isEmpty()) {
                    List<Customer> customers = customerManager.getCustomers();
                    for (Customer customer : customers) {
                        if (customer.getPhoneNumber().equals(phone_text)) {
                            customer.setNote(new_note);  // Cập nhật ghi chú
                            Toast.makeText(use_point_bt2.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            customerManager.saveCustomers(customers);  // Lưu lại thay đổi
                            break;
                        }
                    }
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

                // Kiểm tra nếu không có số điện thoại được nhập
                if (phone_text.isEmpty()) {
                    Toast.makeText(use_point_bt2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;  // Dừng lại nếu số điện thoại trống
                }

                int newPoint = 0;

                // Kiểm tra nếu trường điểm mới (newPoint) có giá trị và chuyển nó thành số nguyên
                if (!newPointStr.isEmpty()) {
                    try {
                        newPoint = Integer.parseInt(newPointStr);  // Chuyển thành int
                    } catch (NumberFormatException e) {
                        Toast.makeText(use_point_bt2.this, "Invalid point value", Toast.LENGTH_SHORT).show();
                        return;  // Dừng nếu điểm nhập không hợp lệ
                    }
                }

                // Kiểm tra nếu cả điểm mới và ghi chú đều trống
                if (new_note.isEmpty() && newPoint == 0) {
                    Toast.makeText(use_point_bt2.this, "New points and note are currently empty", Toast.LENGTH_SHORT).show();
                    return;  // Dừng nếu không có thông tin để cập nhật
                }

                // Cập nhật điểm mới
                if (newPoint != 0) {
                    customerManager.use(newPoint, phone_text);  // tru điểm mới
                }

                // Cập nhật ghi chú mới nếu có
                if (!new_note.isEmpty()) {
                    List<Customer> customers = customerManager.getCustomers();
                    for (Customer customer : customers) {
                        if (customer.getPhoneNumber().equals(phone_text)) {
                            customer.setNote(new_note);  // Cập nhật ghi chú
                            customer.setUpdatedDate(customerManager.getCurrentDateTime());
                            Toast.makeText(use_point_bt2.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            customerManager.saveCustomers(customers);  // Lưu lại thay đổi
                            Intent use_point = new Intent(use_point_bt2.this, danh_sach_kh_bt2.class);
                            startActivity(use_point);  // Thêm startActivity để chuyển trang
                            break;
                        }
                    }
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

