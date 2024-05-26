package com.allformats.video.player.downloader.video_player.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_HideData;

import java.util.ArrayList;
import java.util.List;


public class Vid_player_Database extends SQLiteOpenHelper {
    private static final String COLUMNHIDENAME = "hide_name";
    private static final String COLUMNHIDEPATH = "hide_path";
    private static final String DATABASENAME = "hide_video.db";
    private static final int DATABASEVERSION = 1;
    private static final String TABLEHIDE = "Hide";

    public Vid_player_Database(Context context) {
        super(context, DATABASENAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE Hide ( hide_name TEXT PRIMARY KEY, hide_path TEXT  ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS Hide");
        onCreate(sQLiteDatabase);
    }

    public void addHide(Vid_player_HideData vidplayerHideData) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String replace = vidplayerHideData.getName().replace("'", "@#");
        String replace2 = vidplayerHideData.getPath().replace("'", "@#");
        contentValues.put(COLUMNHIDENAME, replace);
        contentValues.put(COLUMNHIDEPATH, replace2);
        writableDatabase.insert(TABLEHIDE, null, contentValues);
        writableDatabase.close();
    }

    public int getID() {
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT * FROM Hide", null);
        if (rawQuery == null) {
            return 0;
        }
        rawQuery.moveToFirst();
        int count = rawQuery.getCount();
        rawQuery.close();
        return count;
    }

    @SuppressLint("Range")
    public List<Vid_player_HideData> getAllHide() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("select * from Hide", null);
        if (rawQuery.moveToFirst()) {
            do {
                Vid_player_HideData vidplayerHideData = new Vid_player_HideData();
                String replace = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDENAME)).replace("@#", "'");
                String replace2 = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDEPATH)).replace("@#", "'");
                vidplayerHideData.setName(replace);
                vidplayerHideData.setPath(replace2);
                arrayList.add(vidplayerHideData);
            } while (rawQuery.moveToNext());
            rawQuery.close();
            readableDatabase.close();
            return arrayList;
        }
        rawQuery.close();
        readableDatabase.close();
        return arrayList;
    }

    @SuppressLint("Range")
    public Vid_player_HideData getHideData(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Vid_player_HideData vidplayerHideData = null;
        Cursor rawQuery = readableDatabase.rawQuery("select * from Hide where hide_name='" + str + "'", null);
        if (rawQuery.moveToFirst()) {
            String replace = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDENAME)).replace("@#", "'");
            String replace2 = rawQuery.getString(rawQuery.getColumnIndex(COLUMNHIDEPATH)).replace("@#", "'");
            Vid_player_HideData vidplayerHideData2 = new Vid_player_HideData();
            vidplayerHideData2.setName(replace);
            vidplayerHideData2.setPath(replace2);
            vidplayerHideData = vidplayerHideData2;
        }
        rawQuery.close();
        readableDatabase.close();
        return vidplayerHideData;
    }

    public Integer deleteHide(String str) {
        return Integer.valueOf(getWritableDatabase().delete(TABLEHIDE, "hide_name = ?", new String[]{str}));
    }
}
