package com.example.musicplayerservice.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.musicplayerservice.PlayerActivity;
import com.example.musicplayerservice.R;

public class MusicNotificationService extends Service {
    public static final String MUSIC_CHANNEL_ID = "Music_Notification";

    NotificationCompat.Builder builder;
    NotificationManager manager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel musicChannel = new NotificationChannel(
                    MUSIC_CHANNEL_ID,
                    "Music player",
                    NotificationManager.IMPORTANCE_LOW
            );
            musicChannel.setDescription("music notification channel");

            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(musicChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
