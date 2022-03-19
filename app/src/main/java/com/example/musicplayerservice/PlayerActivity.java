package com.example.musicplayerservice;

import static com.example.musicplayerservice.CommonLib.log;
import static com.example.musicplayerservice.CommonLib.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.musicplayerservice.data.Song;
import com.example.musicplayerservice.services.PlayMusicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PlayerActivity extends AppCompatActivity {
    Intent startMusicService;
    private ImageButton prev, play, next, stop;
    private TextView label;
    private SwitchCompat notOnOF;
    ArrayList<Song> playlist = new ArrayList<>();
    Song song;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();

        play = (ImageButton) findViewById(R.id.main_btn_play);
        stop = (ImageButton) findViewById(R.id.main_btn_stop);
        prev = (ImageButton) findViewById(R.id.main_btn_prev);
        next = (ImageButton) findViewById(R.id.main_btn_next);
        label = (TextView) findViewById(R.id.main_tv_label);
        notOnOF = (SwitchCompat) findViewById(R.id.main_sw_notOnOF);

        playlist = ListActivity.playlist;
        position = intent.getIntExtra("pos",0);

        startMusicService = new Intent(PlayerActivity.this, PlayMusicService.class);

        updateUI();


        play.setOnClickListener(v -> {
            updateUI();
        });

        next.setOnClickListener(v -> {
            log(PlayerActivity.this,"before next Pos:" + position + "\n");
            position = (position < playlist.size() -1) ? ++position : 0;
            log(PlayerActivity.this,"after next Pos:" + position + "\n");
            updateUI();
        });

        prev.setOnClickListener(v -> {
            log(PlayerActivity.this,"before prev Pos:" + position + "\n");
            position = (position > 0) ? --position : playlist.size() -1;
            log(PlayerActivity.this,"after prev Pos:" + position + "\n");
            updateUI();
        });

        stop.setOnClickListener(v -> {
            PlayMusicService.stopPlayer();
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
        log(PlayerActivity.this,"in updateUI ZPos:" + position + "\n");
        label.setText(song.getName());
        Uri songPath = Uri.parse(song.getPath());
        startMusicService.putExtra("path", songPath.toString());
        startService(startMusicService);
    }



}