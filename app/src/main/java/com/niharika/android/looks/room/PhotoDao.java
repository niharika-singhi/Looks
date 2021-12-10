package com.niharika.android.looks.room;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Photo> photoList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Photo photo);

    @Query("SELECT * FROM photo_table")
    PagingSource<Integer, Photo> pagingSource();

    @Query("SELECT * FROM photo_table")
    LiveData<List<Photo>> getFavoriteList();

    @Query("DELETE FROM photo_table")
    int clearAll();


}
