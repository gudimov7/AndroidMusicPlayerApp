package com.example.musicplayerservice.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicplayerservice.R;
import com.example.musicplayerservice.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * MusicPlayerService
 * created by
 *
 * @author gudimov
 * on 09 march 2022
 */
public class SongListAdapter extends ArrayAdapter<Song> {

    private final Context context;
    private final int resource;
    private final ArrayList<Song> playlist;

    public SongListAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.playlist = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource, parent, false);

        TextView song = view.findViewById(R.id.row_tv_song);
        Song item = playlist.get(position);
        song.setText(item.getName());

        return view;
    }
}
