package com.niharika.android.looks;

import android.util.Log;

import com.niharika.android.looks.room.Photo;

import java.util.ArrayList;
import java.util.List;
class Photos {
    Integer page;
    String pages,perpage,total;
    public List<Photo> photo;

    public List<Photo> getPhotoList() {
        for(Photo p:photo)
            p.page=page;
        return photo;
    }
}

public class PhotoApiResponse {
    Photos photos;

}