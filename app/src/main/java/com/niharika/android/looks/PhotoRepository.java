package com.niharika.android.looks;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.niharika.android.looks.room.FavoriteDao;
import com.niharika.android.looks.room.FavoritePhoto;
import com.niharika.android.looks.room.Photo;
import com.niharika.android.looks.room.PhotoDao;
import com.niharika.android.looks.room.RoomDb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

interface SearchCallback {
    void onComplete(Boolean result);
}


public class PhotoRepository {
    private FavoriteDao mFavoriteDao;
    private LiveData<List<FavoritePhoto>> mfavoriteList;

    public PhotoRepository(Application application) {
        RoomDb db=RoomDb.getDatabase(application);
        mFavoriteDao=db.favoriteDao();
        mfavoriteList = mFavoriteDao.getFavoriteList();
    }

    void insertFavPhoto(FavoritePhoto photo){
        Executors.newSingleThreadExecutor().execute(()->{
            mFavoriteDao.insert(photo);
        });
    }

    void delFavPhoto(FavoritePhoto photo){
        Executors.newSingleThreadExecutor().execute(()->{
            mFavoriteDao.delFavPhoto(photo.url_s);
        });
    }

    void delAll(){
        Executors.newSingleThreadExecutor().execute(()->{
            mFavoriteDao.clearAll();
        });
    }

    void searchFavoriteList(Photo p, SearchCallback callback){
        Executors.newSingleThreadExecutor().execute(()->{
            Photo result=mFavoriteDao.search(p.url_s);
            if(result!=null)
                callback.onComplete(true);
            else
                callback.onComplete(false);
        });
    }

    LiveData<List<FavoritePhoto>> getFavoriteList()
    {
        return mfavoriteList;
    }
}
