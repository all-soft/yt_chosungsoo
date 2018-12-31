package com.chsapps.yt_nahoonha.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongStorageDB extends SQLiteOpenHelper {
    private static final String TAG = SongStorageDB.class.getSimpleName();

    // DB Version
    public static final int DB_VERSION = 1;

    // DB Name
    public static final String KEYWORD_DB = TAG + ".DB";
    // Table Name
    public static final String DATABASE_TABLE = "TABLE_SONG_STORAGE";

    // Column Name - TABLE_PHONEBOOK
    public static final String COLUMN_SONG_IDX = "song_idx";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_VIDEO_ID = "video_id";
    public static final String COLUMN_PLAY_CNT = "play_cnt";
    public static final String COLUMN_DURATION = "duration";


    public static final String DATABASE_SELECT = "SELECT * FROM " + DATABASE_TABLE;
    public static final String DATABASE_DELETE = "DELETE FROM " + DATABASE_TABLE;

    public SongStorageDB(Context context) {
        super(context, KEYWORD_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        try {
            db.execSQL("PRAGMA cache_size = 5000");
            db.execSQL("PRAGMA read_uncommitted = true");
            db.execSQL("PRAGMA synchronous = OFF");
        }catch (Exception e){

        }
    }

    public static String getQuery() {
        String query = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " (" +
                COLUMN_SONG_IDX + " INTEGER, " +
                COLUMN_THUMBNAIL + " TEXT, " +
                COLUMN_VIDEO_ID + " TEXT UNIQUE, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_PLAY_CNT + " INTEGER)";
        return query;
    }
}
