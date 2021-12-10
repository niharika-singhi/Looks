package com.niharika.android.looks;


import com.google.common.util.concurrent.ListenableFuture;

import retrofit2.http.GET;
import retrofit2.http.Query;

//https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=8f591a4d43458e72fb12a51872a504f0&format=json&nojsoncallback=1.
public interface Api {
    @GET("services/rest")
    ListenableFuture<PhotoApiResponse>
    getAnswers(@Query("method") String method,
               @Query("api_key") String api_key,
               @Query("format") String format,
               @Query("nojsoncallback") String nojsoncallback,
               @Query("extras") String extras,
               @Query("per_page") String per_page,
               @Query("tags") String tags,
               @Query("page") int page);

}

