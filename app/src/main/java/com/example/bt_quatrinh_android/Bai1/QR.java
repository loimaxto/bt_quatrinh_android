package com.example.bt_quatrinh_android.Bai1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bt_quatrinh_android.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
public class QR extends AppCompatActivity {
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.qr);
        new IntentIntegrator(this).initiateScan();

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QR.this, Bai1.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Lấy kết quả quét QR
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Hiển thị hoặc xử lý nội dung của mã QR
                handleQRData(result.getContents());
                Toast.makeText(this, "Nội dung mã QR: " + result.getContents(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Không có kết quả", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleQRData(String data) {
        if (Patterns.WEB_URL.matcher(data).matches()) {
            // Xử lý URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
            startActivity(browserIntent);
        } else if (Patterns.EMAIL_ADDRESS.matcher(data).matches()) {
            // Xử lý email
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + data));
            startActivity(emailIntent);
        } else if (data.startsWith("sms:")) {
            // Xử lý SMS
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(data));
            startActivity(smsIntent);
        } else if (data.startsWith("image:")) {
            // Giả sử data là URL của image
            Intent imageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
            imageIntent.setType("image/*");
            startActivity(imageIntent);
        } else {
            // Hiển thị dữ liệu nếu không xác định được loại
            Toast.makeText(this, "QR data: " + data, Toast.LENGTH_SHORT).show();
        }

//        Intent intent = new Intent(QR.this,Bai1.class);
//        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QR.this, Bai1.class);
        startActivity(intent);
        finish(); // Kết thúc Activity hiện tại
    }



}
