package com.niharika.android.looks.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Photo.class, FavoritePhoto.class}, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {
    private static RoomDb INSTANCE;

    public abstract PhotoDao photoDao();

    public abstract FavoriteDao favoriteDao();

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);

                }
            };

    public static RoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDb.class) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDb.class, "photo_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
            }
        }
        return INSTANCE;
    }
}
