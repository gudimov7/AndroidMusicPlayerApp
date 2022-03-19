package com.example.musicplayerservice;

import static com.example.musicplayerservice.CommonLib.log;
import static com.example.musicplayerservice.CommonLib.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.musicplayerservice.adpaters.SongListAdapter;
import com.example.musicplayerservice.data.Song;
import com.example.musicplayerservice.db.FirebaseST;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    protected static ArrayList<Song> playlist;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.list_lv_playList);

        fetchPlaylist();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            startActivity(
                    new Intent(getApplicationContext(),PlayerActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .putExtra("pos", i)
            );
        });

    }

    private void refreshList() {
        SongListAdapter SLA = new SongListAdapter(ListActivity.this, R.layout.song_list_row, playlist);
        listView.setAdapter(SLA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void fetchPlaylist() {
        if(playlist == null || playlist.isEmpty()) {
            playlist = new ArrayList<>();
            FirebaseST
                    .getStorageRef()
                    .child("songs")
                    .listAll()
                    .addOnSuccessListener(listResult -> {
                        for (StorageReference item: listResult.getItems()) {
                            Song s = new Song();
                            s.setName(item.getName());
                            Task<Uri> downloadURi = item.getDownloadUrl();
                            downloadURi
                                    .addOnSuccessListener(uri -> s.setPath(downloadURi.getResult().toString()))
                                    .addOnFailureListener(e -> log(this, "failed to fetch song: " + e.getCause()));
                            playlist.add(s);
                        }
                        refreshList();
                        toast(ListActivity.this, "Fetched list successfully");
                    })
                    .addOnFailureListener(e -> log(ListActivity.this,"Failed fetching items: " + e.getCause()));

        } else {
            refreshList();
        }
    }
}