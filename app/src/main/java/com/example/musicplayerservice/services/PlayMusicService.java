package com.example.musicplayerservice.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class PlayMusicService extends Service {
    private static MediaPlayer player;


    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(PlayMusicService.this, MusicNotificationService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void stopPlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }
    public static void pausePlayer() {
        if (player.isPlaying())
            player.pause();
        else player.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.hasExtra("path")) {
            Uri songPath = Uri.parse( intent.getExtras().getString("path"));
            if (player != null) {
                stopPlayer();
            }
            if (player == null) {
                player = MediaPlayer.create(PlayMusicService.this, songPath);
                player.setOnCompletionListener(mediaPlayer -> stopPlayer());
            }
            player.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setLooping(false);
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.start();
            });
        }
        return START_STICKY;
    }
}