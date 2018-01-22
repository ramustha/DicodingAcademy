package com.ramusthastudio.mynotesapp;

import android.provider.BaseColumns;

public final class DatabaseContract {
  public static final String TABLE_NOTE = "note";

  private DatabaseContract() {}

  public static final class NoteColumns implements BaseColumns {
    //Note title
    public static final String TITLE = "title";
    //Note description
    public static final String DESCRIPTION = "description";
    //Note date
    public static final String DATE = "date";
  }
}
