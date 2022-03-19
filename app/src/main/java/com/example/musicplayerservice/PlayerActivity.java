package com.example.musicplayerservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.musicplayerservice.data.Song;
import com.example.musicplayerservice.services.MusicNotificationService;
import com.example.musicplayerservice.services.PlayMusicService;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    private Intent startMusicService;
    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<Song> playlist = new ArrayList<>();
    private TextView label;
    private Song song;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();

        notificationManagerCompat = NotificationManagerCompat.from(PlayerActivity.this);
        ImageButton play = (ImageButton) findViewById(R.id.main_btn_play);
        ImageButton stop = (ImageButton) findViewById(R.id.main_btn_stop);
        ImageButton prev = (ImageButton) findViewById(R.id.main_btn_prev);
        ImageButton next = (ImageButton) findViewById(R.id.main_btn_next);
        label = (TextView) findViewById(R.id.main_tv_label);
        SwitchCompat notOnOF = (SwitchCompat) findViewById(R.id.main_sw_notOnOF);
        playlist = ListActivity.playlist;

        position = intent.getIntExtra("pos",0);

        startMusicService = new Intent(PlayerActivity.this, PlayMusicService.class);

        updateUI();
        showNotification();

        play.setOnClickListener(v -> {
            updateUI();
            showNotification();
        });

        stop.setOnClickListener(v -> {
            PlayMusicService.stopPlayer();
            notificationManagerCompat.cancelAll();
        });

        next.setOnClickListener(v -> {
            position = (position < playlist.size() -1) ? ++position : 0;
            updateUI();
        });

        prev.setOnClickListener(v -> {
            position = (position > 0) ? --position : playlist.size() -1;
            updateUI();
        });


        notOnOF.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked)
                showNotification();
            else
                notificationManagerCompat.cancelAll();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_list) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        song = playlist.get(position);
        label.setText(song.getName());
        Uri songPath = Uri.parse(song.getPath());
        startMusicService.putExtra("path", songPath.toString());
        startService(startMusicService);
    }

    private void showNotification() {

        Intent resultIntent = new Intent(PlayerActivity.this, PlayerActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                PlayerActivity.this,
                1,
                resultIntent,
                PendingIntent.FLAG_MUTABLE
        );

        Notification notification = new NotificationCompat
                .Builder(PlayerActivity.this, MusicNotificationService.MUSIC_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setContentTitle("Player")
                .setContentText(song.getName())
                .setColor(Color.GREEN)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(false)
                .setContentIntent(resultPendingIntent)
                .build();

        notificationManagerCompat.notify(1, notification);
    }



}