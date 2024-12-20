package com.example.writelog.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.writelog.HomeActivity;
import com.example.writelog.R;

public class MyService extends Service {

    private static final String CHANNEL_ID = "my_service_channel";
    private static final int NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("My Service is running").setSmallIcon(R.mipmap.ic_launcher);
        startForeground(NOTIFICATION_ID, builder.build());


        Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            }
        startActivity(startIntent);
        Log.d(CHANNEL_ID, " xxxxxxxxxx service  启动首页");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setContentText("Service is running")
                .setSmallIcon(R.mipmap.ic_launcher);
        startForeground(NOTIFICATION_ID, builder.build());
        // 在这里可以添加服务的主要逻辑代码，例如执行一些后台任务
        return START_STICKY;
    }


}
