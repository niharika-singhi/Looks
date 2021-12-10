package com.niharika.android.looks.room;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FavoritePhoto> photoList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavoritePhoto photo);

    @Query("SELECT * FROM favoritePhoto_table")
    PagingSource<Integer, FavoritePhoto> pagingSource();

    @Query("SELECT * FROM favoritePhoto_table where url_s=:url")
    Photo search(String url);

    @Query("SELECT * FROM favoritePhoto_table")
    LiveData<List<FavoritePhoto>> getFavoriteList();

    @Query("DELETE FROM favoritePhoto_table")
    int clearAll();

    @Query("DELETE FROM favoritePhoto_table where url_s=:url")
    int delFavPhoto(String url);
}
