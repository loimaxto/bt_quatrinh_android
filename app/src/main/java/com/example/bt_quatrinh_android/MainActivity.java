package com.example.bt_quatrinh_android;


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

import com.example.bt_quatrinh_android.BT3.AlertReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        TimePicker timePicker = findViewById(R.id.timePicker);
        Button setAlertButton = findViewById(R.id.setAlertButton);

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
                        "Alert successfully set for " + hour + ":" + String.format("%02d", minute),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(this, "Failed to set alert."+hour +minute, Toast.LENGTH_SHORT).show();
            }
        });

        // Create Notification Channel (for Android 8.0+)
        createNotificationChannel();
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
           Toast.makeText(this, "Failed to set alert schedule."+hour +minute, Toast.LENGTH_SHORT).show();
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
