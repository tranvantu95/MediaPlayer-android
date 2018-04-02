package com.rikkeisoft.musicplayer.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Song {

    @PrimaryKey
    private int id;

    public Song(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
