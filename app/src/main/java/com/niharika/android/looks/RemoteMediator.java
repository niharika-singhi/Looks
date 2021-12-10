package com.niharika.android.looks;

import android.util.Log;
import androidx.paging.ListenableFutureRemoteMediator;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.niharika.android.looks.room.Photo;
import com.niharika.android.looks.room.PhotoDao;
import com.niharika.android.looks.room.RoomDb;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.concurrent.Executor;
import retrofit2.HttpException;


class RemoteMediator extends ListenableFutureRemoteMediator<Integer, Photo> {
    private String mQuery;
    private Api networkService;
    private RoomDb database;
    private PhotoDao photoDao;
    private Executor bgExecutor;
    private static final String PER_PAGE = "100";
    private static final String API_KEY = "8f591a4d43458e72fb12a51872a504f0";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.search";

    RemoteMediator(
            String query, Api Service, RoomDb mdatabase, Executor executor
    ) {
        mQuery = query;
        networkService = Service;
        database = mdatabase;
        photoDao = database.photoDao();
        bgExecutor = executor;
    }

    @NotNull
    @Override
    public ListenableFuture<InitializeAction> initializeFuture() {
        return super.initializeFuture();
    }

    @NotNull
    @Override
    public ListenableFuture<MediatorResult> loadFuture(
            @NotNull LoadType loadType,
            @NotNull PagingState<Integer, Photo> state
    ) {
        // The network load method takes an optional after=<user.id> parameter. For
        // every page after the first, pass the last user ID to let it continue from
        // where it left off. For REFRESH, pass null to load the first page.
        Integer loadKey = 0;

        switch (loadType) {
            case REFRESH:
                break;
            case PREPEND:
                // In this example, you never need to prepend, since REFRESH will always
                // load the first page in the list. Immediately return, reporting end of
                // pagination.
                return Futures.immediateFuture(new MediatorResult.Success(true));
            case APPEND:
                Photo lastItem = state.lastItemOrNull();
                // You must explicitly check if the last item is null when appending,
                // since passing null to networkService is only valid for initial load.
                // If lastItem is null it means no items were loaded after the initial
                // REFRESH and there are no more items to load.
                if (lastItem == null)
                    return Futures.immediateFuture(new MediatorResult.Success(true));
                loadKey = lastItem.page;
                break;
        }


        ListenableFuture<MediatorResult> networkResult = Futures.transform(
                networkService.
                        getAnswers(FETCH_RECENTS_METHOD, API_KEY, "json", "1",
                                "url_s", PER_PAGE, mQuery, loadKey+1),
                response -> {
                    database.runInTransaction(() -> {
                        if (loadType == LoadType.REFRESH) {
                            photoDao.clearAll();
                        }
                        photoDao.insertAll(response.photos.getPhotoList());
                    });
                    return new MediatorResult.Success(response.photos.page == null);
                }, bgExecutor);

        ListenableFuture<MediatorResult> ioCatchingNetworkResult =
                Futures.catching(
                        networkResult,
                        IOException.class,
                        MediatorResult.Error::new,
                        bgExecutor
                );
        return Futures.catching(
                ioCatchingNetworkResult,
                HttpException.class,
                MediatorResult.Error::new,
                bgExecutor
        );
    }

}
