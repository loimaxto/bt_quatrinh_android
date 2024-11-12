package com.example.bt_quatrinh_android.Bai1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bt_quatrinh_android.R;

import java.util.ArrayList;

public class callContact extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    ArrayList<DoiTuong> list;
    ListView lv1;
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.callcontacts);
        // Kiểm tra quyền đọc danh bạ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu người dùng cấp quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {
            // Nếu đã có quyền, tải danh bạ
            lv1 = findViewById(R.id.listview);
            list = getContacts();
            DoiTuongAdapter adapter = new DoiTuongAdapter(callContact.this,R.layout.dong_doi_tuong,list);
            lv1.setAdapter(adapter);
            Button btn = findViewById(R.id.button3);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(callContact.this,Bai1.class);
                    startActivity(intent);
                }
            });
        }

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy tên của phần tử được nhấn
                String selectedItem = list.get(position).getName();
                String contactId = list.get(position).contactID();
                Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(contactUri);
                startActivity(intent);
                Toast.makeText(callContact.this, "Bạn đã nhấn vào: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //ham lay danh sach danh ba
    public ArrayList<DoiTuong> getContacts()
    {
        ArrayList<DoiTuong> contacts = new ArrayList<>();
        Cursor cursor = null;
        cursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor != null && cursor.moveToFirst())
        {
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            do{
                String contactId = cursor.getString(idIndex);
                String contactName = cursor.getString(nameIndex);
                Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId},null);
                if(phoneCursor !=null && phoneCursor.moveToFirst())
                {
                    do{
                        int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = phoneCursor.getString(phoneIndex);
                        contacts.add(new DoiTuong(phoneNumber,contactName,contactId));
                    }while(phoneCursor.moveToNext());
                }
                phoneCursor.close();
            }while(cursor.moveToNext());
            cursor.close();
        }
        return contacts;
    }


}
