package com.chsapps.yt_hongjinyoung.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.chsapps.yt_hongjinyoung.app.AllSoft;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;

import rx.schedulers.Schedulers;

public class StorageDBHelper extends LockExecutorTemplate {
    private static final String TAG = StorageDBHelper.class.getSimpleName();

    private static class StorageDBHelperLazy {
        private static final StorageDBHelper instance = new StorageDBHelper();
    }

    private SqlBrite sqlBrite;
    private BriteDatabase briteDatabase;

    public static StorageDBHelper getInstance() {
        return StorageDBHelperLazy.instance;
    }

    private StorageDBHelper() {
        sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
            }
        });
        briteDatabase = sqlBrite.wrapDatabaseHelper(new SongStorageDB(AllSoft.getContext()), Schedulers.io());
    }

    private SongData getData(Cursor cursor) {
        return new SongData(
                cursor.getInt(cursor.getColumnIndex(SongStorageDB.COLUMN_SONG_IDX)),
                cursor.getString(cursor.getColumnIndex(SongStorageDB.COLUMN_THUMBNAIL)),
                cursor.getString(cursor.getColumnIndex(SongStorageDB.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(SongStorageDB.COLUMN_VIDEO_ID)),
                cursor.getInt(cursor.getColumnIndex(SongStorageDB.COLUMN_PLAY_CNT)),
                cursor.getString(cursor.getColumnIndex(SongStorageDB.COLUMN_DURATION))
        );
    }

    public int getCount() {
        return execute(new Executor<Integer>() {
            @Override
            public Integer execute() {
                Integer count = 0;
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(SongStorageDB.DATABASE_SELECT, "");
                    if (cursor != null) {
                        count = cursor.getCount();
                    }
                } catch (Exception e) {
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                return count;
            }
        });
    }

    public ArrayList<SongData> getList() {
        return execute(new Executor<ArrayList<SongData>>() {
            @Override
            public ArrayList<SongData> execute() {
                ArrayList<SongData> list = new ArrayList<>();
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(SongStorageDB.DATABASE_SELECT, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        while(true) {
                            list.add(getData(cursor));
                            if(!cursor.moveToNext())
                                break;
                        }
                    }
                } catch (Exception e) {
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                return list;
            }
        });
    }

    public boolean removeData(final SongData song) {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                try {
                    int ret = briteDatabase.delete(SongStorageDB.DATABASE_TABLE, SongStorageDB.COLUMN_VIDEO_ID + " = '" + song.getRealVideoId() + "'", null);
                } catch (Exception e) {
                } finally {
                }
                return true;
            }
        });
    }
    public boolean addData(final SongData song) {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                SongData data = null;
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(SongStorageDB.DATABASE_SELECT + " WHERE " + SongStorageDB.COLUMN_VIDEO_ID + " = '" + song.getRealVideoId() + "'", null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            data = getData(cursor);
                        }
                    }
                } catch (Exception e) {
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                if (data == null) {
                    ContentValues initialValues = new ContentValues();
                    initialValues.put(SongStorageDB.COLUMN_SONG_IDX, song.song_idx);
                    initialValues.put(SongStorageDB.COLUMN_THUMBNAIL, song.thumbnail);
                    initialValues.put(SongStorageDB.COLUMN_TITLE, song.title);
                    initialValues.put(SongStorageDB.COLUMN_PLAY_CNT, song.play_cnt);
                    initialValues.put(SongStorageDB.COLUMN_VIDEO_ID, song.getRealVideoId());
                    initialValues.put(SongStorageDB.COLUMN_DURATION, song.getDuration());
                    return briteDatabase.insert(SongStorageDB.DATABASE_TABLE, initialValues) > 0;
                } else {
                    ContentValues initialValues = new ContentValues();
                    initialValues.put(SongStorageDB.COLUMN_SONG_IDX, song.song_idx);
                    initialValues.put(SongStorageDB.COLUMN_THUMBNAIL, song.thumbnail);
                    initialValues.put(SongStorageDB.COLUMN_TITLE, song.title);
                    initialValues.put(SongStorageDB.COLUMN_PLAY_CNT, song.play_cnt);
                    initialValues.put(SongStorageDB.COLUMN_VIDEO_ID, song.getRealVideoId());
                    initialValues.put(SongStorageDB.COLUMN_DURATION, song.getDuration());
                    return briteDatabase.update(SongStorageDB.DATABASE_TABLE, initialValues, SongStorageDB.COLUMN_VIDEO_ID + " = '" + data.getRealVideoId() + "'") > 0;
                }
            }
        });
    }

    public boolean deleteAll() {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                return briteDatabase.delete(SongStorageDB.DATABASE_TABLE, "") > 0;
            }
        });
    }
}
