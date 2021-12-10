package com.niharika.android.looks;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import com.niharika.android.looks.room.FavoritePhoto;
import com.niharika.android.looks.room.Photo;
import com.niharika.android.looks.room.PhotoDao;
import com.niharika.android.looks.room.RoomDb;
import java.util.List;
import java.util.concurrent.Executors;
import kotlinx.coroutines.CoroutineScope;


public class ItemViewModel extends AndroidViewModel {
    LiveData<PagingData<Photo>> liveData;
    ItemDataSource photoPagingSource;
    PhotoRepository mPhotoRepository;
    RoomDb database;
    PhotoDao photoDao;
    String mQuery;
    Integer PAGE_SIZE, PREFETCH_DISTANCE;
    CoroutineScope viewModelScope;


    public ItemViewModel(Application application) {
        super(application);
        photoPagingSource = new ItemDataSource(RetrofitClient.getInsance().getApi(), null, Executors.newSingleThreadExecutor());
        viewModelScope = ViewModelKt.getViewModelScope(this);
        mPhotoRepository = new PhotoRepository(application);
        database = RoomDb.getDatabase(application);
        photoDao = database.photoDao();
        mQuery = application.getResources().getString(R.string.query);
        PAGE_SIZE = Integer.parseInt(application.getResources().getString(R.string.page_size));
        PREFETCH_DISTANCE = Integer.parseInt(application.getResources().getString(R.string.prefetch));
        createPager(mQuery);
    }

    LiveData<List<FavoritePhoto>> getFavoriteList() {
        return mPhotoRepository.getFavoriteList();
    }

    void createPager(String query) {
        Pager pager = new Pager(
                new PagingConfig(PAGE_SIZE, PREFETCH_DISTANCE, true), null,
                new RemoteMediator(query, RetrofitClient.getInsance().getApi(), database, Executors.newSingleThreadExecutor()),
                () -> photoDao.pagingSource());
        liveData = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }
}