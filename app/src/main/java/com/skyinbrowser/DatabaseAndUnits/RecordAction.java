package com.skyinbrowser.DatabaseAndUnits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Query;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecordAction {
    private SQLiteDatabase database;
    private RecordHelper helper;

    public RecordAction(Context context) {
        this.helper = new RecordHelper(context);
    }

    public void open(boolean rw) {
        if (rw) {
            database = helper.getWritableDatabase();
        } else {
            database = helper.getReadableDatabase();
        }
    }

    public void close() {
        helper.close();
    }

    public boolean addBookmark(Record record) {
        if (record == null
                || record.getTitle() == null
                || record.getTitle().trim().isEmpty()
                || record.getURL() == null
                || record.getURL().trim().isEmpty()
                || record.getTime() < 0l) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, record.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, record.getURL().trim());
        values.put(RecordUnit.COLUMN_TIME, record.getTime());
        database.insert(RecordUnit.TABLE_BOOKMARKS, null, values);

        return true;
    }

    public boolean addNewsBookmark(NewsBookmarkRecord record) {
        if (record == null
                || record.getTitle() == null
                || record.getTitle().trim().isEmpty()
                || record.getURL() == null
                || record.getURL().trim().isEmpty()
                || record.getSource() == null
                || record.getSource().trim().isEmpty()
                || record.getLanguage() == null
                || record.getLanguage().trim().isEmpty()
                || record.getImage()==null
                || record.getImage().trim().isEmpty()
                || record.getTime() < 0l) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, record.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, record.getURL().trim());
        values.put(RecordUnit.COLUMN_SOURCE, record.getSource().trim());
        values.put(RecordUnit.COLUMN_LANGUAGE, record.getLanguage().trim());
        values.put(RecordUnit.COLUMN_IMAGE, record.getImage().trim());
        values.put(RecordUnit.COLUMN_DESCRIPTION, record.getDescription().trim());
        values.put(RecordUnit.COLUMN_TIME, record.getTime());
        database.insert(RecordUnit.TABLE_NEWS_BOOKMARKS, null, values);

        return true;
    }

    public boolean addHistory(Record record) {
        if (record == null
                || record.getTitle() == null
                || record.getTitle().trim().isEmpty()
                || record.getURL() == null
                || record.getURL().trim().isEmpty()
                || record.getTime() < 0l) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, record.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, record.getURL().trim());
        values.put(RecordUnit.COLUMN_TIME, record.getTime());

        if (hasUrl(record.getURL().trim())){
            database.update(RecordUnit.TABLE_HISTORY, values, RecordUnit.COLUMN_TIME + "=?", new String[] {String.valueOf(record.getTime())});
        }else {
            database.insert(RecordUnit.TABLE_HISTORY, null, values);
        }
        return true;
    }

    public boolean hasUrl(String url){
        String queryCheck="SELECT * FROM " + RecordUnit.TABLE_HISTORY +
                " WHERE "+RecordUnit.COLUMN_URL+" = '"+url+"'";
        Cursor cursor=database.rawQuery(queryCheck,null);

        boolean hasUrl = false;
        if (cursor.moveToFirst()){
            hasUrl=true;
            int count=0;
            while(cursor.moveToNext()){
                count++;
            }
        }
        cursor.close();
        return hasUrl;
    }

    public boolean addFloatHistory(Record record) {
        if (record == null
                || record.getTitle() == null
                || record.getTitle().trim().isEmpty()
                || record.getURL() == null
                || record.getURL().trim().isEmpty()
                || record.getTime() < 0l) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, record.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, record.getURL().trim());
        values.put(RecordUnit.COLUMN_TIME, record.getTime());
        if (hasUrl(record.getURL().trim())){
            database.update(RecordUnit.TABLE_FLOAT_HISTORY, values, RecordUnit.COLUMN_TIME + "=?", new String[] {String.valueOf(record.getTime())});
        }else {
            database.insert(RecordUnit.TABLE_FLOAT_HISTORY, null, values);
        }
        return true;
    }

    public boolean addUpperSplitHistory(Record record) {
        if (record == null
                || record.getTitle() == null
                || record.getTitle().trim().isEmpty()
                || record.getURL() == null
                || record.getURL().trim().isEmpty()
                || record.getTime() < 0l) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, record.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, record.getURL().trim());
        values.put(RecordUnit.COLUMN_TIME, record.getTime());
        if (hasUrl(record.getURL().trim())){
            database.update(RecordUnit.TABLE_UPPER_SPLIT_HISTORY, values, RecordUnit.COLUMN_TIME + "=?", new String[] {String.valueOf(record.getTime())});
        }else {
            database.insert(RecordUnit.TABLE_UPPER_SPLIT_HISTORY, null, values);
        }

        return true;
    }

    public boolean addLowerSplitHistory(Record record) {
        if (record == null
                || record.getTitle() == null
                || record.getTitle().trim().isEmpty()
                || record.getURL() == null
                || record.getURL().trim().isEmpty()
                || record.getTime() < 0l) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, record.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, record.getURL().trim());
        values.put(RecordUnit.COLUMN_TIME, record.getTime());
        if (hasUrl(record.getURL().trim())){
            database.update(RecordUnit.TABLE_LOWER_SPLIT_HISTORY, values, RecordUnit.COLUMN_TIME + "=?", new String[] {String.valueOf(record.getTime())});
        }else {
            database.insert(RecordUnit.TABLE_LOWER_SPLIT_HISTORY, null, values);
        }

        return true;
    }

    public boolean addDomain(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_DOMAIN, domain.trim());
        database.insert(RecordUnit.TABLE_WHITELIST, null, values);

        return true;
    }

    public boolean addGridItem(GridItem item) {
        if (item == null
                || item.getTitle() == null
                || item.getTitle().trim().isEmpty()
                || item.getURL() == null
                || item.getURL().trim().isEmpty()
                || item.getFilename() == null
                || item.getFilename().trim().isEmpty()
                || item.getOrdinal() < 0) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, item.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, item.getURL().trim());
        values.put(RecordUnit.COLUMN_FILENAME, item.getFilename().trim());
        values.put(RecordUnit.COLUMN_ORDINAL, item.getOrdinal());
        database.insert(RecordUnit.TABLE_GRID, null, values);

        return true;
    }

    public boolean updateBookmark(Record record) {
        if (record == null
                || record.getTitle() == null
                || record.getTitle().trim().isEmpty()
                || record.getURL() == null
                || record.getURL().trim().isEmpty()
                || record.getTime() < 0) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, record.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, record.getURL().trim());
        values.put(RecordUnit.COLUMN_TIME, record.getTime());
        database.update(RecordUnit.TABLE_BOOKMARKS, values, RecordUnit.COLUMN_TIME + "=?", new String[] {String.valueOf(record.getTime())});

        return true;
    }

    public boolean updateGridItem(GridItem item) {
        if (item == null
                || item.getTitle() == null
                || item.getTitle().trim().isEmpty()
                || item.getURL() == null
                || item.getURL().trim().isEmpty()
                || item.getFilename() == null
                || item.getFilename().trim().isEmpty()
                || item.getOrdinal() < 0) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(RecordUnit.COLUMN_TITLE, item.getTitle().trim());
        values.put(RecordUnit.COLUMN_URL, item.getURL().trim());
        values.put(RecordUnit.COLUMN_FILENAME, item.getFilename().trim());
        values.put(RecordUnit.COLUMN_ORDINAL, item.getOrdinal());
        database.update(RecordUnit.TABLE_GRID, values, RecordUnit.COLUMN_URL + "=?", new String[] {item.getURL()});

        return true;
    }

    public boolean checkBookmark(Record record) {
        if (record == null || record.getURL() == null || record.getURL().trim().isEmpty()) {
            return false;
        }

        Cursor cursor = database.query(
                RecordUnit.TABLE_BOOKMARKS,
                new String[] {RecordUnit.COLUMN_URL},
                RecordUnit.COLUMN_URL + "=?",
                new String[] {record.getURL().trim()},
                null,
                null,
                null
        );

        if (cursor != null) {
            boolean result = false;
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();

            return result;
        }

        return false;
    }

    public boolean checkBookmark(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        Cursor cursor = database.query(
                RecordUnit.TABLE_BOOKMARKS,
                new String[] {RecordUnit.COLUMN_URL},
                RecordUnit.COLUMN_URL + "=?",
                new String[] {url.trim()},
                null,
                null,
                null
        );

        if (cursor != null) {
            boolean result = false;
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();

            return result;
        }

        return false;
    }

    public boolean checkDomain(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return false;
        }

        Cursor cursor = database.query(
                RecordUnit.TABLE_WHITELIST,
                new String[] {RecordUnit.COLUMN_DOMAIN},
                RecordUnit.COLUMN_DOMAIN + "=?",
                new String[] {domain.trim()},
                null,
                null,
                null
        );

        if (cursor != null) {
            boolean result = false;
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();

            return result;
        }

        return false;
    }

    public boolean checkGridItem(GridItem item) {
        if (item == null || item.getURL() == null || item.getURL().trim().isEmpty()) {
            return false;
        }

        Cursor cursor = database.query(
                RecordUnit.TABLE_GRID,
                new String[] {RecordUnit.COLUMN_URL},
                RecordUnit.COLUMN_URL + "=?",
                new String[] {item.getURL().trim()},
                null,
                null,
                null
        );

        if (cursor != null) {
            boolean result = false;
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();

            return result;
        }

        return false;
    }

    public boolean checkGridItem(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        Cursor cursor = database.query(
                RecordUnit.TABLE_GRID,
                new String[] {RecordUnit.COLUMN_URL},
                RecordUnit.COLUMN_URL + "=?",
                new String[] {url.trim()},
                null,
                null,
                null
        );

        if (cursor != null) {
            boolean result = false;
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();

            return result;
        }

        return false;
    }

    public boolean deleteBookmark(Record record) {
        if (record == null || record.getURL() == null || record.getURL().trim().isEmpty()) {
            return false;
        }

        database.execSQL("DELETE FROM " + RecordUnit.TABLE_BOOKMARKS + " WHERE " + RecordUnit.COLUMN_URL + " = " + "\"" + record.getURL().trim() + "\"");
        return true;
    }

    public boolean deleteNewsBookmark(NewsBookmarkRecord record) {
        if (record == null || record.getURL() == null || record.getURL().trim().isEmpty()) {
            return false;
        }

        database.execSQL("DELETE FROM " + RecordUnit.TABLE_NEWS_BOOKMARKS + " WHERE " + RecordUnit.COLUMN_URL + " = " + "\"" + record.getURL().trim() + "\"");
        return true;
    }

    public boolean deleteHistory(Record record) {
        if (record == null || record.getTime() <= 0) {
            return false;
        }

        database.execSQL("DELETE FROM "+ RecordUnit.TABLE_HISTORY + " WHERE " + RecordUnit.COLUMN_TIME + " = " + record.getTime());
        return true;
    }

    public boolean deleteFloatHistory(Record record) {
        if (record == null || record.getTime() <= 0) {
            return false;
        }

        database.execSQL("DELETE FROM "+ RecordUnit.TABLE_FLOAT_HISTORY + " WHERE " + RecordUnit.COLUMN_TIME + " = " + record.getTime());
        return true;
    }

    public boolean deleteUpperSplitHistory(Record record) {
        if (record == null || record.getTime() <= 0) {
            return false;
        }

        database.execSQL("DELETE FROM "+ RecordUnit.TABLE_UPPER_SPLIT_HISTORY + " WHERE " + RecordUnit.COLUMN_TIME + " = " + record.getTime());
        return true;
    }

    public boolean deleteLowerSplitHistory(Record record) {
        if (record == null || record.getTime() <= 0) {
            return false;
        }

        database.execSQL("DELETE FROM "+ RecordUnit.TABLE_LOWER_SPLIT_HISTORY + " WHERE " + RecordUnit.COLUMN_TIME + " = " + record.getTime());
        return true;
    }

    public boolean deleteDomain(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return false;
        }

        database.execSQL("DELETE FROM "+ RecordUnit.TABLE_WHITELIST + " WHERE " + RecordUnit.COLUMN_DOMAIN + " = " + "\"" + domain.trim() + "\"");
        return true;
    }

    public boolean deleteGridItem(GridItem item) {
        if (item == null || item.getURL() == null || item.getURL().trim().isEmpty()) {
            return false;
        }

        database.execSQL("DELETE FROM " + RecordUnit.TABLE_GRID + " WHERE " + RecordUnit.COLUMN_URL + " = " + "\"" + item.getURL().trim() + "\"");
        return true;
    }

    public boolean deleteGridItem(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        database.execSQL("DELETE FROM "+ RecordUnit.TABLE_GRID + " WHERE " + RecordUnit.COLUMN_URL + " = " + "\"" + url.trim() + "\"");
        return true;
    }

    public void clearBookmarks() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_BOOKMARKS);
    }

    public void clearNewsBookmarks() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_NEWS_BOOKMARKS);
    }

    public void clearHistory() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_HISTORY);
    }

    public void clearFloatHistory() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_FLOAT_HISTORY);
    }

    public void clearUpperSplitHistory() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_UPPER_SPLIT_HISTORY);
    }

    public void clearLowerSplitHistory() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_LOWER_SPLIT_HISTORY);
    }

    public void clearDomains() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_WHITELIST);
    }

    public void clearGrid() {
        database.execSQL("DELETE FROM " + RecordUnit.TABLE_GRID);
    }

    private Record getRecord(Cursor cursor) {
        Record record = new Record();
        record.setTitle(cursor.getString(0));
        record.setURL(cursor.getString(1));
        record.setTime(cursor.getLong(2));

        return record;
    }

    private NewsBookmarkRecord getNewsRecord(Cursor cursor) {
        NewsBookmarkRecord record = new NewsBookmarkRecord();
        record.setTitle(cursor.getString(0));
        record.setURL(cursor.getString(1));
        record.setSource(cursor.getString(2));
        record.setLanguage(cursor.getString(3));
        record.setImage(cursor.getString(4));
        record.setDescription(cursor.getString(5));
        record.setTime(cursor.getLong(6));

        return record;
    }

    private GridItem getGridItem(Cursor cursor) {
        GridItem item = new GridItem();
        item.setTitle(cursor.getString(0));
        item.setURL(cursor.getString(1));
        item.setFilename(cursor.getString(2));
        item.setOrdinal(cursor.getInt(3));

        return item;
    }

    public List<Record> listBookmarks() {
        List<Record> list = new ArrayList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_BOOKMARKS,
                new String[] {
                        RecordUnit.COLUMN_TITLE,
                        RecordUnit.COLUMN_URL,
                        RecordUnit.COLUMN_TIME
                },
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_TIME + " desc"
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public List<NewsBookmarkRecord> listNewsBookmarks() {
        List<NewsBookmarkRecord> list = new ArrayList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_NEWS_BOOKMARKS,
                new String[] {
                        RecordUnit.COLUMN_TITLE,
                        RecordUnit.COLUMN_URL,
                        RecordUnit.COLUMN_SOURCE,
                        RecordUnit.COLUMN_LANGUAGE,
                        RecordUnit.COLUMN_IMAGE,
                        RecordUnit.COLUMN_DESCRIPTION,
                        RecordUnit.COLUMN_TIME
                },
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_TIME + " desc"
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getNewsRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public List<Record> listHistory() {
        List<Record> list = new ArrayList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_HISTORY,
                new String[] {
                        RecordUnit.COLUMN_TITLE,
                        RecordUnit.COLUMN_URL,
                        RecordUnit.COLUMN_TIME
                },
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_TIME + " desc"
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public List<Record> listFloatHistory() {
        List<Record> list = new ArrayList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_FLOAT_HISTORY,
                new String[] {
                        RecordUnit.COLUMN_TITLE,
                        RecordUnit.COLUMN_URL,
                        RecordUnit.COLUMN_TIME
                },
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_TIME + " desc"
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public List<Record> listUpperSplitHistory() {
        List<Record> list = new ArrayList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_UPPER_SPLIT_HISTORY,
                new String[] {
                        RecordUnit.COLUMN_TITLE,
                        RecordUnit.COLUMN_URL,
                        RecordUnit.COLUMN_TIME
                },
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_TIME + " desc"
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public List<Record> listLowerSplitHistory() {
        List<Record> list = new ArrayList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_LOWER_SPLIT_HISTORY,
                new String[] {
                        RecordUnit.COLUMN_TITLE,
                        RecordUnit.COLUMN_URL,
                        RecordUnit.COLUMN_TIME
                },
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_TIME + " desc"
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public List<String> listDomains() {
        List<String> list = new ArrayList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_WHITELIST,
                new String[] {RecordUnit.COLUMN_DOMAIN},
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_DOMAIN
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public List<GridItem> listGrid() {
        List<GridItem> list = new LinkedList<>();

        Cursor cursor = database.query(
                RecordUnit.TABLE_GRID,
                new String[] {
                        RecordUnit.COLUMN_TITLE,
                        RecordUnit.COLUMN_URL,
                        RecordUnit.COLUMN_FILENAME,
                        RecordUnit.COLUMN_ORDINAL
                },
                null,
                null,
                null,
                null,
                RecordUnit.COLUMN_ORDINAL
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(getGridItem(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }
}
