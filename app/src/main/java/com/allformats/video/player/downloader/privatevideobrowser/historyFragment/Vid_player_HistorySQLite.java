package com.allformats.video.player.downloader.privatevideobrowser.historyFragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Vid_player_HistorySQLite extends SQLiteOpenHelper {
    private final String VISITED_PAGES = "visited_pages";
    private final SQLiteDatabase dB = getWritableDatabase();

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public Vid_player_HistorySQLite(Context context) {
        super(context, "history.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override 
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE visited_pages (title TEXT, link TEXT, time TEXT)");
    }

    public void addPageToHistory(Vid_player_VisitedPage vidplayerVisitedPage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", vidplayerVisitedPage.title);
        contentValues.put("link", vidplayerVisitedPage.link);
        contentValues.put("time", new SimpleDateFormat("yyyy MM dd HH mm ss SSS", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        SQLiteDatabase sQLiteDatabase = this.dB;
        if (sQLiteDatabase.update("visited_pages", contentValues, "link = '" + vidplayerVisitedPage.link + "'", null) <= 0) {
            this.dB.insert("visited_pages", null, contentValues);
        }
    }

    public void deleteFromHistory(String str) {
        SQLiteDatabase sQLiteDatabase = this.dB;
        sQLiteDatabase.delete("visited_pages", "link = '" + str + "'", null);
    }

    public void clearHistory() {
        this.dB.execSQL("DELETE FROM visited_pages");
    }

    @SuppressLint("Range")
    public List<Vid_player_VisitedPage> getAllVisitedPages() {
        Cursor query = this.dB.query("visited_pages", new String[]{"title", "link"}, null, null, null, null, "time DESC");
        ArrayList arrayList = new ArrayList();
        while (query.moveToNext()) {
            Vid_player_VisitedPage vidplayerVisitedPage = new Vid_player_VisitedPage();
            vidplayerVisitedPage.title = query.getString(query.getColumnIndex("title"));
            vidplayerVisitedPage.link = query.getString(query.getColumnIndex("link"));
            arrayList.add(vidplayerVisitedPage);
        }
        query.close();
        return arrayList;
    }
    @SuppressLint("Range")
    public List<Vid_player_VisitedPage> getVisitedPagesByKeyword(String str) {
        Cursor query = this.dB.query("visited_pages", new String[]{"title", "link"}, "title LIKE '%" + str + "%'", null, null, null, "time DESC");
        ArrayList arrayList = new ArrayList();
        while (query.moveToNext()) {
            Vid_player_VisitedPage vidplayerVisitedPage = new Vid_player_VisitedPage();
            vidplayerVisitedPage.title = query.getString(query.getColumnIndex("title"));
            vidplayerVisitedPage.link = query.getString(query.getColumnIndex("link"));
            arrayList.add(vidplayerVisitedPage);
        }
        query.close();
        return arrayList;
    }
}
