package com.example.bt_quatrinh_android.Bai1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt_quatrinh_android.R;

public class Bai1 extends AppCompatActivity {
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main_bai1);
        Button callContact = findViewById(R.id.callContact);
        Button callQR = findViewById(R.id.callQR);

        //ham xu li click callContact
        callContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itCallContact = new Intent(Bai1.this,callContact.class);
                startActivity(itCallContact);
            }
        });

        //ham xu li click callQR
        callQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
