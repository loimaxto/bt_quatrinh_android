package com.example.bt_quatrinh_android.BT3;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;

import com.example.bt_quatrinh_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class bt3_main extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int REQUEST_SET_NOTIFICATION_TIME = 1;
    private static final int REQUEST_CODE_PERMISSION = 100;

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<Image> imageList;
    private DatabaseHelper databaseHelper;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.bt3_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        imageList = loadImageByDate();
        adapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openCamera());
    }

    private List<Image> loadImageByDate (){
        return databaseHelper.getAllImagesGroupedByDate();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (data != null && data.getExtras() != null) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        saveImage(image);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void saveImage(Bitmap image) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(new Date());

        boolean isSaved = databaseHelper.saveImage(image, currentDate);
        if (isSaved) {
            Toast.makeText(this,"Sucessfully store image",Toast.LENGTH_SHORT).show();
            imageList.clear();
            imageList.addAll(loadImageByDate());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.bt3_toolbar_menu,m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_selected) {
            handleDeleteSelected();
            return true;
        } else if (item.getItemId() == R.id.delete_all) {
            handleDeleteAll();
            return true;
        } else if (item.getItemId() == R.id.reminder) {
            handleReminder();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleDeleteSelected() {
        List<Image> selectedImages = adapter.getSelectedImages();
        for( Image image : selectedImages) {
            databaseHelper.deleteImage(image);
        }
        refreshImageList();
        Toast.makeText(this,"Delete selected images", Toast.LENGTH_SHORT).show();

    }
    private void handleDeleteAll() {
        databaseHelper.deleteAllImages();
        refreshImageList();
        Toast.makeText(this,"Delete all images", Toast.LENGTH_SHORT).show();

    }

    private void refreshImageList() {
        imageList.clear();
        imageList.addAll(databaseHelper.getAllImagesGroupedByDate());
        adapter.notifyDataSetChanged();
    }

    private void handleReminder() {
        Intent intent = new Intent(bt3_main.this, SetReminderActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setNotification(int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("message", "Đến giờ chụp ảnh rồi");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE); //PendingIntent.FLAG_UPDATE_CURRENT

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager == null) {
            Toast.makeText(this, hour + ":" + minute, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (Exception e) {
//            e.getStackTrace();
        }
        Toast.makeText(this, "Thông báo đã được thiết lập", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền đã được cấp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cần cấp quyền để thiết lập thông báo chính xác", Toast.LENGTH_SHORT).show();
            }
        }
    }
}