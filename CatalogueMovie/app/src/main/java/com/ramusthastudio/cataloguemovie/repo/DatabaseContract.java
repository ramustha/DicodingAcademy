package com.ramusthastudio.cataloguemovie.repo;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
  public static final String TABLE_NAME = "moviedb";
  public static final int VERSION = 1;
  public static final String AUTHORITY = "com.ramusthastudio.cataloguemovie";
  public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
      .authority(AUTHORITY)
      .appendPath(TABLE_NAME)
      .build();

  private DatabaseContract() {}

  public static String getColumnString(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }

  public static int getColumnInt(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName));
  }

  public static long getColumnLong(Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndex(columnName));
  }

  public static double getColumnDouble(Cursor cursor, String columnName) {
    return cursor.getDouble(cursor.getColumnIndex(columnName));
  }

  public static float getColumnFloat(Cursor cursor, String columnName) {
    return cursor.getFloat(cursor.getColumnIndex(columnName));
  }

  public static final class MovieColumns implements BaseColumns {
    public static final String MOVIE_ID = "movie_id";
    public static final String TITLE = "title";
    public static final String POSTER = "poster";
    public static final String BACKDROP = "backdrop";
    public static final String RELEASE_DATE = "release_date";
    public static final String GENRE = "genre";
    public static final String RATING = "rating";
    public static final String OVERVIEW = "overview";
    public static final String POPULARITY = "popularity";
  }
}
