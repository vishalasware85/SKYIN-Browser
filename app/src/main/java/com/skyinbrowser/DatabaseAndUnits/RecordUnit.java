package com.skyinbrowser.DatabaseAndUnits;

public class RecordUnit {
    public static final String TABLE_BOOKMARKS = "BOOKMARKS";
    public static final String TABLE_HISTORY = "HISTORY";
    public static final String TABLE_FLOAT_HISTORY = "FLOAT_HISTORY";
    public static final String TABLE_UPPER_SPLIT_HISTORY = "UPPER_SPLIT_HISTORY";
    public static final String TABLE_LOWER_SPLIT_HISTORY = "LOWER_SPLIT_HISTORY";
    public static final String TABLE_WHITELIST = "WHITELIST";
    public static final String TABLE_GRID = "GRID";
    public static final String TABLE_NEWS_BOOKMARKS = "NEWS_BOOKMARKS";

    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_SOURCE = "SOURCE";
    public static final String COLUMN_LANGUAGE = "LANGUAGE";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_DOMAIN = "DOMAIN";
    public static final String COLUMN_FILENAME = "FILENAME";
    public static final String COLUMN_ORDINAL = "ORDINAL";

    public static final String CREATE_HISTORY = "CREATE TABLE "
            + TABLE_HISTORY
            + " ("
            + " " + COLUMN_TITLE + " text,"
            + " " + COLUMN_URL + " text,"
            + " " + COLUMN_TIME + " integer"
            + ")";

    public static final String CREATE_FLOAT_HISTORY = "CREATE TABLE "
            + TABLE_FLOAT_HISTORY
            + " ("
            + " " + COLUMN_TITLE + " text,"
            + " " + COLUMN_URL + " text,"
            + " " + COLUMN_TIME + " integer"
            + ")";

    public static final String CREATE_UPPER_SPLIT_HISTORY = "CREATE TABLE "
            + TABLE_UPPER_SPLIT_HISTORY
            + " ("
            + " " + COLUMN_TITLE + " text,"
            + " " + COLUMN_URL + " text,"
            + " " + COLUMN_TIME + " integer"
            + ")";

    public static final String CREATE_LOWER_SPLIT_HISTORY = "CREATE TABLE "
            + TABLE_LOWER_SPLIT_HISTORY
            + " ("
            + " " + COLUMN_TITLE + " text,"
            + " " + COLUMN_URL + " text,"
            + " " + COLUMN_TIME + " integer"
            + ")";

    public static final String CREATE_BOOKMARKS = "CREATE TABLE "
            + TABLE_BOOKMARKS
            + " ("
            + " " + COLUMN_TITLE + " text,"
            + " " + COLUMN_URL + " text,"
            + " " + COLUMN_TIME + " integer"
            + ")";

    public static final String CREATE_WHITELIST = "CREATE TABLE "
            + TABLE_WHITELIST
            + " ("
            + " " + COLUMN_DOMAIN + " text"
            + ")";

    public static final String CREATE_GRID = "CREATE TABLE "
            + TABLE_GRID
            + " ("
            + " " + COLUMN_TITLE + " text,"
            + " " + COLUMN_URL + " text,"
            + " " + COLUMN_FILENAME + " text,"
            + " " + COLUMN_ORDINAL + " integer"
            + ")";

    public static final String CREATE_NEWS_BOOKMARKS = "CREATE TABLE "
            + TABLE_NEWS_BOOKMARKS
            + " ("
            + " " + COLUMN_TITLE + " text,"
            + " " + COLUMN_URL + " text,"
            + " " + COLUMN_SOURCE + " text,"
            + " " + COLUMN_LANGUAGE + " text,"
            + " " + COLUMN_IMAGE + " text,"
            + " " + COLUMN_DESCRIPTION + " text,"
            + " " + COLUMN_TIME + " integer"
            + ")";

    private static Record holder;
    public static Record getHolder() {
        return holder;
    }
    public synchronized static void setHolder(Record record) {
        holder = record;
    }

    private static NewsBookmarkRecord holderNews;
    public static NewsBookmarkRecord getHolderNews() {
        return holderNews;
    }
    public synchronized static void setHolderNews(NewsBookmarkRecord record) {
        holderNews = record;
    }
}
