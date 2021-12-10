package com.niharika.android.looks.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class Photo implements Comparable<Photo> {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "owner")
    public String owner;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "url_s")
    public String url_s;
    @ColumnInfo(name = "page")
    public int page;

    public Photo(@NonNull String id, String owner, String title, String url_s, Integer page) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.url_s = url_s;
        this.page = page;
    }

    @Override
    public int compareTo(Photo o) {
        if (id.compareTo(o.id) == 0 && owner == o.owner
                && title == o.title
                && url_s == o.url_s
                && page == o.page) {
            return 0;
        } else {
            return 1;
        }
    }
}
