package com.chsapps.yt_hongjinyoung.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KeywordDB extends SQLiteOpenHelper {
    private static final String TAG = KeywordDB.class.getSimpleName();

    // DB Version
    public static final int DB_VERSION = 1;

    // DB Name
    public static final String KEYWORD_DB = "KEYWORD.DB";
    // Table Name
    public static final String DATABASE_TABLE = "TABLE_KEYWORD";

    // Column Name - TABLE_PHONEBOOK
    public static final String COLUMN_KEYWORD = "keyword";

    public static final String DATABASE_SELECT = "SELECT * FROM " + DATABASE_TABLE;
    public static final String DATABASE_DELETE = "DELETE FROM " + DATABASE_TABLE;

    public KeywordDB(Context context) {
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
                COLUMN_KEYWORD + " TEXT UNIQUE)";
        return query;
    }
}
