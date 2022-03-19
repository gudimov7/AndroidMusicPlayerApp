package com.example.musicplayerservice.data;

/**
 * MusicPlayerService
 * created by
 *
 * @author gudimov
 * on 09 march 2022
 */
public class Song {
    String name, path;

    public Song(String name) {
        setName(name);
    }

    public Song() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name
                .trim()
                .replaceAll("[[.mp3] || [-mp3]]*$","")
                .replaceAll("[[\\-] || [\\_]]"," ");
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                '}';
    }
}
