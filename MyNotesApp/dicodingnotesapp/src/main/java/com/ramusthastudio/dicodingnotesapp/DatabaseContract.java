package com.ramusthastudio.dicodingnotesapp;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {
  public static final String TABLE_NOTE = "note";
  public static final String AUTHORITY = "com.ramusthastudio.mynotesapp";
  public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
      .authority(AUTHORITY)
      .appendPath(TABLE_NOTE)
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

  public static final class NoteColumns implements BaseColumns {
    //Note title
    public static final String TITLE = "title";
    //Note description
    public static final String DESCRIPTION = "description";
    //Note date
    public static final String DATE = "date";
  }
}
