package com.chsapps.yt_hongjinyoung.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.chsapps.yt_hongjinyoung.app.yt7080;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;

import rx.schedulers.Schedulers;

public class KeywordDBHelper extends LockExecutorTemplate {
    private static final String TAG = KeywordDBHelper.class.getSimpleName();

    private static class StorageDBHelperLazy {
        private static final KeywordDBHelper instance = new KeywordDBHelper();
    }

    private SqlBrite sqlBrite;
    private BriteDatabase briteDatabase;

    public static KeywordDBHelper getInstance() {
        return StorageDBHelperLazy.instance;
    }

    private KeywordDBHelper() {
        sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
            }
        });
        briteDatabase = sqlBrite.wrapDatabaseHelper(new KeywordDB(yt7080.getContext()), Schedulers.io());
    }

    private String getData(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(KeywordDB.COLUMN_KEYWORD));
    }

    public int getCount() {
        return execute(new Executor<Integer>() {
            @Override
            public Integer execute() {
                Integer count = 0;
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(KeywordDB.DATABASE_SELECT, "");
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

    public ArrayList<String> getList() {
        return execute(new Executor<ArrayList<String>>() {
            @Override
            public ArrayList<String> execute() {
                ArrayList<String> list = new ArrayList<>();
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(KeywordDB.DATABASE_SELECT, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        while (true) {
                            list.add(getData(cursor));
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
                return list;
            }
        });
    }

    public boolean removeData(final String keyword) {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                try {
                    int ret = briteDatabase.delete(KeywordDB.DATABASE_TABLE, KeywordDB.COLUMN_KEYWORD + " = '" + keyword + "'", null);
                } catch (Exception e) {
                } finally {
                }
                return true;
            }
        });
    }

    public boolean addData(final String keyword) {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                String data = null;
                Cursor cursor = null;
                try {
                    cursor = briteDatabase.query(KeywordDB.DATABASE_SELECT + " WHERE " + KeywordDB.COLUMN_KEYWORD + " = '" + keyword + "'", null);
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
                    initialValues.put(KeywordDB.COLUMN_KEYWORD, keyword);
                    return briteDatabase.insert(KeywordDB.DATABASE_TABLE, initialValues) > 0;
                }
                return true;
            }
        });
    }

    public boolean deleteAll() {
        return execute(new Executor<Boolean>() {
            @Override
            public Boolean execute() {
                return briteDatabase.delete(KeywordDB.DATABASE_TABLE, "") > 0;
            }
        });
    }
}
