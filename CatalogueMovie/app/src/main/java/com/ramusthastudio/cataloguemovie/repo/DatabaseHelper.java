package com.ramusthastudio.cataloguemovie.repo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.TABLE_NAME;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.VERSION;

final class DatabaseHelper extends SQLiteOpenHelper {

  DatabaseHelper(Context context) {
    super(context, TABLE_NAME, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(String.format("CREATE TABLE %s"
            + " (%s INT PRIMARY KEY," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s LONG," +
            " %s TEXT NOT NULL," +
            " %s DOUBLE," +
            " %s TEXT NOT NULL," +
            " %s FLOAT)",
        DatabaseContract.TABLE_NAME,
        DatabaseContract.MovieColumns._ID,
        DatabaseContract.MovieColumns.TITLE,
        DatabaseContract.MovieColumns.POSTER,
        DatabaseContract.MovieColumns.BACKDROP,
        DatabaseContract.MovieColumns.RELEASE_DATE,
        DatabaseContract.MovieColumns.GENRE,
        DatabaseContract.MovieColumns.RATING,
        DatabaseContract.MovieColumns.OVERVIEW,
        DatabaseContract.MovieColumns.POPULARITY
    ));
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }
}
