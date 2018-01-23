package com.ramusthastudio.kamus.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ramusthastudio.kamus.repository.KamusDatabaseContract.KamusColumn;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.kamus.repository.KamusDatabaseContract.DATABASE_NAME;
import static com.ramusthastudio.kamus.repository.KamusDatabaseContract.TABLE_ENGLISH_INDONESIA;
import static com.ramusthastudio.kamus.repository.KamusDatabaseContract.TABLE_INDONESIA_ENGLISH;

final class KamusDatabaseHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;

  KamusDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
        "create table " + TABLE_ENGLISH_INDONESIA +
            " (" + _ID + " integer primary key autoincrement, " +
            KamusColumn.WORD + " text not null, " +
            KamusColumn.DESCRIPTION + " text not null);");

    db.execSQL(
        "create table " + TABLE_INDONESIA_ENGLISH +
            " (" + _ID + " integer primary key autoincrement, " +
            KamusColumn.WORD + " text not null, " +
            KamusColumn.DESCRIPTION + " text not null);");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENGLISH_INDONESIA);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDONESIA_ENGLISH);
    onCreate(db);
  }
}
