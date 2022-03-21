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
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.musicplayerservice.data.Song;
import com.example.musicplayerservice.services.MusicNotificationService;
import com.example.musicplayerservice.services.NotificationClickReceiver;
import com.example.musicplayerservice.services.PlayMusicService;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    private Intent startMusicService;
    private RemoteViews collapsedNotification;
    private NotificationManagerCompat notificationManagerCompat;
    private ArrayList<Song> playlist = new ArrayList<>();
    private TextView label;
    private TextView labelNotification;
    private Song song;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();

        notificationManagerCompat = NotificationManagerCompat.from(PlayerActivity.this);
        collapsedNotification = new RemoteViews(getPackageName(), R.layout.notification_collapsed);

        ImageButton play = (ImageButton) findViewById(R.id.main_btn_play);
        ImageButton stop = (ImageButton) findViewById(R.id.main_btn_stop);
        ImageButton prev = (ImageButton) findViewById(R.id.main_btn_prev);
        ImageButton next = (ImageButton) findViewById(R.id.main_btn_next);
        label = (TextView) findViewById(R.id.main_tv_label);
        SwitchCompat notOnOF = (SwitchCompat) findViewById(R.id.main_sw_notOnOF);

        ImageButton playNotification = (ImageButton) findViewById(R.id.ib_pauseBtn);
        ImageButton stopNotification = (ImageButton) findViewById(R.id.ib_stopBtn);
        ImageButton prevNotification = (ImageButton) findViewById(R.id.ib_prevBtn);
        ImageButton nextNotification = (ImageButton) findViewById(R.id.ib_nextBtn);
        labelNotification = (TextView) findViewById(R.id.tv_song_title);

        playlist = ListActivity.playlist;

        position = intent.getIntExtra("pos",0);

        startMusicService = new Intent(PlayerActivity.this, PlayMusicService.class);

        updateUI();
        handleNotification();

        play.setOnClickListener(v -> play());
        stop.setOnClickListener(v -> stop());
        next.setOnClickListener(v -> next());
        prev.setOnClickListener(v -> prev());

        notOnOF.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked)
                handleNotification();
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
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private  void updateUI() {
        song = playlist.get(position);
        label.setText(song.getName());
        collapsedNotification.setTextViewText(R.id.tv_song_title, song.getName());
        Uri songPath = Uri.parse(song.getPath());
        startMusicService.putExtra("path", songPath.toString());
        startService(startMusicService);
    }

    private void handleNotification() {
        //intent to return to player activity on notification click
        Intent resultIntent = new Intent(PlayerActivity.this, PlayerActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                PlayerActivity.this,
                1,
                resultIntent,
                PendingIntent.FLAG_MUTABLE
        );

        //intent to listen on click action on notification
        Intent pauseBtn = new Intent(PlayerActivity.this, NotificationClickReceiver.class);
        pauseBtn.setAction("pause");
        PendingIntent pauseClickPendingIntent = PendingIntent.getBroadcast(PlayerActivity.this, 1, pauseBtn, PendingIntent.FLAG_MUTABLE);

        Intent stopBtn = new Intent(PlayerActivity.this, NotificationClickReceiver.class);
        stopBtn.setAction("stop");
        PendingIntent stopClickedPendingIntent = PendingIntent.getBroadcast(PlayerActivity.this, 1, stopBtn, PendingIntent.FLAG_MUTABLE);

        Intent nextBtn = new Intent(PlayerActivity.this, NotificationClickReceiver.class);
        nextBtn.setAction("next");
        PendingIntent nextClickedPendingIntent = PendingIntent.getBroadcast(PlayerActivity.this, 1, nextBtn, PendingIntent.FLAG_MUTABLE);

        Intent prevBtn = new Intent(PlayerActivity.this, NotificationClickReceiver.class);
        prevBtn.setAction("prev");
        PendingIntent prevClickedPendingIntent = PendingIntent.getBroadcast(PlayerActivity.this, 1, prevBtn, PendingIntent.FLAG_MUTABLE);

        collapsedNotification.setOnClickPendingIntent(R.id.ib_pauseBtn, pauseClickPendingIntent);
        collapsedNotification.setOnClickPendingIntent(R.id.ib_stopBtn, stopClickedPendingIntent);
        collapsedNotification.setOnClickPendingIntent(R.id.ib_nextBtn, nextClickedPendingIntent);
        collapsedNotification.setOnClickPendingIntent(R.id.ib_prevBtn, prevClickedPendingIntent);


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
                .setCustomContentView(collapsedNotification)
                .setCustomBigContentView(collapsedNotification)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    public void play() {
        updateUI();
        handleNotification();
    }
    public void stop() {
        PlayMusicService.stopPlayer();
        notificationManagerCompat.cancelAll();
    }
    public void pause() {
        PlayMusicService.pausePlayer();
    }
    public void prev() {
        position = (position > 0) ? --position : playlist.size() -1;
        updateUI();
    }
    public void next() {
        position = (position < playlist.size() -1) ? ++position : 0;
        updateUI();
    }



}