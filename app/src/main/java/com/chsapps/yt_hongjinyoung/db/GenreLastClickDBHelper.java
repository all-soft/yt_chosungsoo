package com.chsapps.yt_hongjinyoung.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.chsapps.yt_hongjinyoung.app.yt7080;
import com.chsapps.yt_hongjinyoung.data.GenreLastClickData;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.HashMap;
import java.util.Map;

import rx.schedulers.Schedulers;

public class GenreLastClickDBHelper extends LockExecutorTemplate {
    private static final String TAG = GenreLastClickDBHelper.class.getSimpleName();

    private static class StorageDBHelperLazy {
        private static final GenreLastClickDBHelper instance = new GenreLastClickDBHelper();
    }

    private SqlBrite sqlBrite;
    private BriteDatabase briteDatabase;

    public static GenreLastClickDBHelper getInstance() {
        return StorageDBHelperLazy.instance;
    }

    private GenreLastClickDBHelper() {
        sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
            }
        });
        briteDatabase = sqlBrite.wrapDatabaseHelper(new GenreLastClickDB(yt7080.getContext()), Schedulers.io());
    }

    private GenreLastClickData getData(Cursor cursor) {
        return new GenreLastClickData(
                cursor.getString(cursor.getColumnIndex(GenreLastClickDB.COLUMN_CATEGORY_ID)),
                cursor.getLong(cursor.getColumnIndex(GenreLastClickDB.COLUMN_CATEGORY_CLICK_TIME)));
    }

    public int getCount() {
        return execute(new Executor<Integer>() {
            @Override
            public Integer execute() {
                Integer count = 0;
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(GenreLastClickDB.DATABASE_SELECT, "");
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

    public Map<String, Long> getAllData() {
        return execute(new Executor<Map<String, Long>>() {
            @Override
            public Map<String, Long> execute() {
                Map<String, Long> mapData = new HashMap<>();
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(GenreLastClickDB.DATABASE_SELECT, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        while (true) {
                            GenreLastClickData data = getData(cursor);
                            mapData.put(data.id, data.lastClickTime);
                            if(!cursor.moveToNext()) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                return mapData;
            }
        });
    }

    public boolean insert(final String id) {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                GenreLastClickData data = null;
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(GenreLastClickDB.DATABASE_SELECT + " WHERE " + GenreLastClickDB.COLUMN_CATEGORY_ID + " = '" + id + "'", null);
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
                ContentValues initialValues = new ContentValues();
                initialValues.put(GenreLastClickDB.COLUMN_CATEGORY_ID, id);
                initialValues.put(GenreLastClickDB.COLUMN_CATEGORY_CLICK_TIME, System.currentTimeMillis());
                if (data == null) {
                    return briteDatabase.insert(GenreLastClickDB.DATABASE_TABLE, initialValues) > 0;
                } else {
                    return briteDatabase.update(GenreLastClickDB.DATABASE_TABLE, initialValues, GenreLastClickDB.COLUMN_CATEGORY_ID + " = '" + id + "'") > 0;
                }
            }
        });
    }

    public boolean deleteAll() {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                return briteDatabase.delete(GenreLastClickDB.DATABASE_TABLE, "") > 0;
            }
        });
    }
}
