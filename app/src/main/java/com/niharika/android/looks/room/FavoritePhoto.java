package com.niharika.android.looks.room;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="favoritePhoto_table")
public class FavoritePhoto {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="id")
    public String id;
    @ColumnInfo(name="owner")
    public String owner;
    @ColumnInfo(name="title")
    public String title;
    @ColumnInfo(name="url_s")
    public String url_s;
    @ColumnInfo(name="page")
    public int page;

    public FavoritePhoto(@NonNull String id, String owner, String title, String url_s) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.url_s = url_s;
    }
}
