package com.chsapps.yt_hongjinyoung.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.chsapps.yt_hongjinyoung.app.yt7080;
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
        briteDatabase = sqlBrite.wrapDatabaseHelper(new StorageDB(yt7080.getContext()), Schedulers.io());
    }

    private SongData getData(Cursor cursor) {
        return new SongData(
                cursor.getInt(cursor.getColumnIndex(StorageDB.COLUMN_SONG_IDX)),
                cursor.getString(cursor.getColumnIndex(StorageDB.COLUMN_THUMBNAIL)),
                cursor.getString(cursor.getColumnIndex(StorageDB.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(StorageDB.COLUMN_VIDEO_ID)),
                cursor.getInt(cursor.getColumnIndex(StorageDB.COLUMN_PLAY_CNT)),
                cursor.getString(cursor.getColumnIndex(StorageDB.COLUMN_DURATION))
        );
    }

    public int getCount() {
        return execute(new Executor<Integer>() {
            @Override
            public Integer execute() {
                Integer count = 0;
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(StorageDB.DATABASE_SELECT, "");
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
                    cursor = briteDatabase.query(StorageDB.DATABASE_SELECT, null);
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
                    int ret = briteDatabase.delete(StorageDB.DATABASE_TABLE, StorageDB.COLUMN_VIDEO_ID + " = '" + song.getVideoid() + "'", null);
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
                    cursor = briteDatabase.query(StorageDB.DATABASE_SELECT + " WHERE " + StorageDB.COLUMN_VIDEO_ID + " = '" + song.videoid + "'", null);
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

                try {
                    if (data == null) {
                        ContentValues initialValues = new ContentValues();
                        initialValues.put(StorageDB.COLUMN_SONG_IDX, song.song_idx);
                        initialValues.put(StorageDB.COLUMN_THUMBNAIL, song.thumbnail);
                        initialValues.put(StorageDB.COLUMN_TITLE, song.title);
                        initialValues.put(StorageDB.COLUMN_PLAY_CNT, song.play_cnt);
                        initialValues.put(StorageDB.COLUMN_VIDEO_ID, song.videoid);
                        initialValues.put(StorageDB.COLUMN_DURATION, song.getDuration());
                        return briteDatabase.insert(StorageDB.DATABASE_TABLE, initialValues) > 0;
                    } else {
                        ContentValues initialValues = new ContentValues();
                        initialValues.put(StorageDB.COLUMN_SONG_IDX, song.song_idx);
                        initialValues.put(StorageDB.COLUMN_THUMBNAIL, song.thumbnail);
                        initialValues.put(StorageDB.COLUMN_TITLE, song.title);
                        initialValues.put(StorageDB.COLUMN_PLAY_CNT, song.play_cnt);
                        initialValues.put(StorageDB.COLUMN_VIDEO_ID, song.videoid);
                        initialValues.put(StorageDB.COLUMN_DURATION, song.getDuration());
                        return briteDatabase.update(StorageDB.DATABASE_TABLE, initialValues, StorageDB.COLUMN_VIDEO_ID + " = '" + data.getVideoid() + "'") > 0;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public boolean deleteAll() {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                return briteDatabase.delete(StorageDB.DATABASE_TABLE, "") > 0;
            }
        });
    }
}
