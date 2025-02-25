package com.example.bt_quatrinh_android.BT3;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt_quatrinh_android.R;

import java.util.Calendar;

public class SetReminderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt3_timepicker);

        //huy thong tbao
        Intent intent = getIntent();
        if (intent != null && "CANCEL_ALERT".equals(intent.getAction())) {
            cancelNotification();
            finish(); // Đóng activity sau khi hủy
            return;
        }

        TimePicker timePicker = findViewById(R.id.timePicker);
        Button setAlertButton = findViewById(R.id.btnSetReminder);

        // Ensure the user is setting time in 24-hour mode
        timePicker.setIs24HourView(true);

        setAlertButton.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            // Schedule the notification
            boolean isScheduled = scheduleNotification(hour, minute);

            // Notify user if scheduling was successful
            if (isScheduled) {
                Toast.makeText(
                        this,
                        "Đặt nhắc nhở thành công lúc " + hour + ":" + String.format("%02d", minute),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(this, "Failed to set alert."+hour +minute, Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        // Create Notification Channel (for Android 8.0+)
        createNotificationChannel();

    }

    public void cancelNotification() {
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent); // Hủy báo thức
        }

        Toast.makeText(this, "Nhắc nhở đã được hủy", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ScheduleExactAlarm")
    private boolean scheduleNotification(int hour, int minute) {

        // Set the notification time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            // Adjust time to the next day if set time is in the past
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Create an intent for the BroadcastReceiver
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        try {
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }


            return true; // Indicate success
        } catch (Exception e) {
            e.printStackTrace();
             return false; // Indicate failure
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ALERT_CHANNEL",
                    "Alert Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for time-based alerts");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

