package com.example.bt2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bt2.model.Customer;
import com.example.bt2.model.CustomerManager;
import com.example.bt2.ui.theme.XmlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class input_point_bt2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_point_bt2);

        EditText phonenumber = findViewById(R.id.numberphope);
        TextView curr_point = findViewById(R.id.curr_point);
        EditText newPointEditText = findViewById(R.id.new_point);
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

        // Sự kiện cho nút import
        Button importBtn = findViewById(R.id.import_btn);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importCustomersToSharedPreferences();
            }
        });


        // Sự kiện cho nút save
        Button save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String new_note = noteEditText.getText().toString().trim();
                String newPointStr = newPointEditText.getText().toString().trim();
                String phone_text = phonenumber.getText().toString().trim();

                // Kiểm tra nếu không có số điện thoại được nhập
                if (phone_text.isEmpty()) {
                    Toast.makeText(input_point_bt2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;  // Dừng lại nếu số điện thoại trống
                }

                int newPoint = 0;

                // Kiểm tra nếu trường điểm mới (newPoint) có giá trị và chuyển nó thành số nguyên
                if (!newPointStr.isEmpty()) {
                    try {
                        newPoint = Integer.parseInt(newPointStr);  // Chuyển thành int
                    } catch (NumberFormatException e) {
                        Toast.makeText(input_point_bt2.this, "Invalid point value", Toast.LENGTH_SHORT).show();
                        return;  // Dừng nếu điểm nhập không hợp lệ
                    }
                }

                // Kiểm tra nếu cả điểm mới và ghi chú đều trống
                if (new_note.isEmpty() && newPoint == 0) {
                    Toast.makeText(input_point_bt2.this, "New points and note are currently empty", Toast.LENGTH_SHORT).show();
                    return;  // Dừng nếu không có thông tin để cập nhật
                }

                // Cập nhật điểm mới
                if (newPoint != 0) {
                    customerManager.plus(newPoint, phone_text);  // Cộng điểm mới
                }

                // Cập nhật ghi chú mới nếu có
                if (!new_note.isEmpty()) {
                    List<Customer> customers = customerManager.getCustomers();
                    for (Customer customer : customers) {
                        if (customer.getPhoneNumber().equals(phone_text)) {
                            customer.setNote(new_note);  // Cập nhật ghi chú
                            Toast.makeText(input_point_bt2.this, "Successfully updated", Toast.LENGTH_SHORT).show();
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
                String newPointStr = newPointEditText.getText().toString().trim();
                String phone_text = phonenumber.getText().toString().trim();

                // Kiểm tra nếu không có số điện thoại được nhập
                if (phone_text.isEmpty()) {
                    Toast.makeText(input_point_bt2.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;  // Dừng lại nếu số điện thoại trống
                }

                int newPoint = 0;

                // Kiểm tra nếu trường điểm mới (newPoint) có giá trị và chuyển nó thành số nguyên
                if (!newPointStr.isEmpty()) {
                    try {
                        newPoint = Integer.parseInt(newPointStr);  // Chuyển thành int
                    } catch (NumberFormatException e) {
                        Toast.makeText(input_point_bt2.this, "Invalid point value", Toast.LENGTH_SHORT).show();
                        return;  // Dừng nếu điểm nhập không hợp lệ
                    }
                }

                // Kiểm tra nếu cả điểm mới và ghi chú đều trống
                if (new_note.isEmpty() && newPoint == 0) {
                    Toast.makeText(input_point_bt2.this, "New points and note are currently empty", Toast.LENGTH_SHORT).show();
                    return;  // Dừng nếu không có thông tin để cập nhật
                }

                // Cập nhật điểm mới
                if (newPoint != 0) {
                    customerManager.plus(newPoint, phone_text);  // Cộng điểm mới
                }

                // Cập nhật ghi chú mới nếu có
                if (!new_note.isEmpty()) {
                    List<Customer> customers = customerManager.getCustomers();
                    for (Customer customer : customers) {
                        if (customer.getPhoneNumber().equals(phone_text)) {
                            customer.setNote(new_note);  // Cập nhật ghi chú
                            customer.setUpdatedDate(customerManager.getCurrentDateTime());
                            Toast.makeText(input_point_bt2.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            customerManager.saveCustomers(customers);  // Lưu lại thay đổi
                            Intent use_point = new Intent(input_point_bt2.this, use_point_bt2.class);
                            startActivity(use_point);  // Thêm startActivity để chuyển trang
                            break;
                        }
                    }
                }
            }
        });


        //hàm exports danh sách
        Button exportButton = findViewById(R.id.export_btn);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlParser.exportCustomersToXML(v.getContext()); // Xuất danh sách ra XML
                XmlParser.sendEmailWithXMLFile(v.getContext()); // Gửi email với file XML đính kèm
            }
        });

        // Sự kiện cho các nút chuyển trang
        Button input_btn = findViewById(R.id.input_btn);
        input_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        Button use_btn = findViewById(R.id.use_btn);
        use_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent use_point = new Intent(input_point_bt2.this, use_point_bt2.class);
                startActivity(use_point);  // Thêm startActivity để chuyển trang
            }
        });

        Button list_btn = findViewById(R.id.list_btn);
        list_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent use_point = new Intent(input_point_bt2.this, danh_sach_kh_bt2.class);
                startActivity(use_point);  // Thêm startActivity để chuyển trang
            }
        });

        //tạo sự kiện nút logout
        Button logout_btn = findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent use_point = new Intent(input_point_bt2.this, login_bt2.class);
                startActivity(use_point);  // Thêm startActivity để chuyển trang
            }
        });

    }

    // Hàm nhập danh sách khách hàng từ file XML và lưu vào SharedPreferences
    private void importCustomersToSharedPreferences() {
        try {
            List<Customer> newCustomers = XmlParser.parseCustomersFromXml(this, R.raw.customers);

            CustomerManager customerManager = new CustomerManager(this);
            List<Customer> existingCustomers = customerManager.getCustomers();

            if (existingCustomers == null) {
                existingCustomers = new ArrayList<>();
            } else {
                existingCustomers = new ArrayList<>(existingCustomers);
            }

            for (Customer newCustomer : newCustomers) {
                boolean exists = false;
                for (Customer existingCustomer : existingCustomers) {
                    if (existingCustomer.getPhoneNumber().equals(newCustomer.getPhoneNumber())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    existingCustomers.add(newCustomer);
                }
            }

            customerManager.saveCustomers(existingCustomers);
            Toast.makeText(input_point_bt2.this, "Customers imported successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ImportError", "Failed to import customers: " + e.getMessage(), e);
            Toast.makeText(input_point_bt2.this, "Failed to import customers.", Toast.LENGTH_SHORT).show();
        }
    }





}
