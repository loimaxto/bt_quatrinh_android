package com.example.bt2.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerManager {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "CustomerPrefs";
    private Context context; // Thêm biến context
    private static final String KEY_CUSTOMERS = "customers";

    public CustomerManager(Context context) {
        this.context = context; // Lưu lại context được truyền vào

        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Lưu danh sách khách hàng vào SharedPreferences
    public void saveCustomers(List<Customer> customers) {
        Gson gson = new Gson();
        String json = gson.toJson(customers);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CUSTOMERS, json);
        editor.apply();
    }

    // Lấy danh sách khách hàng từ SharedPreferences
    public List<Customer> getCustomers() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CUSTOMERS, null);
        if (json != null) {
            Customer[] customersArray = gson.fromJson(json, Customer[].class);
            return Arrays.asList(customersArray);
        }
        return new ArrayList<>();
    }

    public void clearCustomers(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CUSTOMERS); // Xóa riêng mục 'customers'
        editor.apply();
    }

    public void add(Customer customer) {
        List<Customer> customers = getCustomers();
        customers.add(customer);
        saveCustomers(customers);
    }
    public void remove(String phoneNumber) {
        List<Customer> customers = getCustomers();
        // Tạo danh sách mới sau khi xóa khách hàng
        List<Customer> updatedCustomers = new ArrayList<>();
        // Duyệt qua danh sách khách hàng
        for (Customer customer : customers) {
            // Nếu số điện thoại không khớp, giữ lại khách hàng trong danh sách mới
            if (!customer.getPhoneNumber().equals(phoneNumber)) {
                updatedCustomers.add(customer);
            }
        }
        // Lưu lại danh sách khách hàng sau khi xóa vào SharedPreferences
        saveCustomers(updatedCustomers);
    }

    public void use(int point, String phoneNumber) {
        // Lấy danh sách khách hàng hiện tại từ SharedPreferences
        List<Customer> customers = getCustomers();

        boolean foundCustomer = false;

        // Duyệt qua danh sách khách hàng để tìm khách hàng với số điện thoại tương ứng
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                foundCustomer = true;

                // Kiểm tra nếu khách hàng có đủ điểm để sử dụng
                if (customer.getPoint() >= point) {
                    // Trừ điểm của khách hàng
                    customer.setPoint(customer.getPoint() - point);

                    // Hiển thị thông báo thành công bằng Toast
                    Toast.makeText(context, "Successfully used " + point + " points for customer " + phoneNumber, Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu điểm không đủ, hiển thị thông báo bằng Toast
                    Toast.makeText(context, "Customer does not have enough points.", Toast.LENGTH_SHORT).show();
                }
                break; // Thoát khỏi vòng lặp sau khi tìm thấy khách hàng
            }
        }

        // Nếu không tìm thấy khách hàng
        if (!foundCustomer) {
            Toast.makeText(context, "Customer with phone number " + phoneNumber + " not found.", Toast.LENGTH_SHORT).show();
        } else {
            // Lưu danh sách khách hàng đã cập nhật vào SharedPreferences
            saveCustomers(customers);
        }
    }


    public void plus(int point, String phoneNumber) {
        // Lấy danh sách khách hàng hiện tại từ SharedPreferences
        List<Customer> customers = getCustomers();

        boolean foundCustomer = false;

        // Duyệt qua danh sách khách hàng để tìm khách hàng với số điện thoại tương ứng
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                foundCustomer = true;

                    // Cộng điểm của khách hàng
                    int new_point = customer.getPoint() + point;
                    customer.setPoint(new_point);
                    customer.setUpdatedDate(getCurrentDateTime());
                    // Hiển thị thông báo thành công bằng Toast
                    Toast.makeText(context, "Successfully used " + point + " points for customer " + phoneNumber, Toast.LENGTH_SHORT).show();
                break; // Thoát khỏi vòng lặp sau khi tìm thấy khách hàng
            }
        }

        // Nếu không tìm thấy khách hàng
        if (!foundCustomer) {
            Toast.makeText(context, "Customer with phone number " + phoneNumber + " not found.", Toast.LENGTH_SHORT).show();
        } else {
            // Lưu danh sách khách hàng đã cập nhật vào SharedPreferences
            saveCustomers(customers);
        }
    }


    public String getCurrentDateTime() {
        // Lấy thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Định dạng ngày tháng năm và giờ theo kiểu "YYYY-MM-DD HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Trả về chuỗi đã định dạng
        return now.format(formatter);
    }
}



