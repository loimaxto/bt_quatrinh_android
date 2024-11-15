package com.example.bt_quatrinh_android.BT3;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.bt_quatrinh_android.R;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Hiển thị thông báo khi báo thức kích hoạt
        Toast.makeText(context, "It is time to take picture!", Toast.LENGTH_SHORT).show();
    }
}
