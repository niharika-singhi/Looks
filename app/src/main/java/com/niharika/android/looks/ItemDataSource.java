package com.niharika.android.looks;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagingState;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.niharika.android.looks.room.Photo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class ItemDataSource extends ListenableFuturePagingSource<Integer, Photo> {

    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;
    private static final String text= "painting";
    private static final String ACCESS_TOKEN = "stackoverflow";
    private static final String API_KEY = "8f591a4d43458e72fb12a51872a504f0";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.search";
    @NonNull
    private Api mBackend;
    @NonNull
    private String mQuery;
    @NonNull
    private Executor mBgExecutor;

    ItemDataSource(
            @NonNull Api backend,
            @NonNull String query, @NonNull Executor bgExecutor) {
        mBackend = backend;
        mQuery = query;
        mBgExecutor = bgExecutor;
    }

    @NotNull
    @Override
    public ListenableFuture<LoadResult<Integer, Photo>> loadFuture(@NotNull LoadParams<Integer> params) {
        // Start refresh at page 1 if undefined.
        Integer nextPageNumber = params.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }
       // Log.d("Looks","Page num is"+nextPageNumber);
        //Allows  to access a result of task that has already been completed or might complete in future
        ListenableFuture<PhotoApiResponse> mResponse=mBackend.getAnswers(FETCH_RECENTS_METHOD, API_KEY,
                "json", "1",
                "url_s","3",text,nextPageNumber);

    //convert the result of the listenablefuture once successfull
             ListenableFuture<LoadResult<Integer, Photo>> pageFuture =
                Futures.transform(mResponse,
                        this::toLoadResult, mBgExecutor);

        ListenableFuture<LoadResult<Integer, Photo>> partialLoadResultFuture =
                Futures.catching(pageFuture, HttpException.class,
                        LoadResult.Error::new, mBgExecutor);

        return Futures.catching(partialLoadResultFuture,
                IOException.class, LoadResult.Error::new, mBgExecutor);


    }

    private LoadResult<Integer,Photo> toLoadResult(@NonNull PhotoApiResponse response) {

        Integer page=response.photos.page;
        return new LoadResult.Page<>(response.photos.getPhotoList(),
                page == 1 ? null : page - 1,
                page+1,
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, Photo> state) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            Log.d("Looks","Refresh"+anchorPosition);
            return null;
        }

        LoadResult.Page<Integer, Photo> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            Log.d("Looks","Refresh"+anchorPage);
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            Log.d("Looks","Refresh"+prevKey+1);
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            Log.d("Looks","Refresh"+(nextKey-1));
            return nextKey - 1;
        }

        return null;
    }

}

