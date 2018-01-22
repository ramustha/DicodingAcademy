package com.ramusthastudio.mypreloaddata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.mypreloaddata.DatabaseContract.MahasiswaColumns.NAMA;
import static com.ramusthastudio.mypreloaddata.DatabaseContract.MahasiswaColumns.NIM;
import static com.ramusthastudio.mypreloaddata.DatabaseContract.TABLE_NAME;

final class DatabaseHelper extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "dbmahasiswa";
  private static final int DATABASE_VERSION = 1;
  public static String CREATE_TABLE_MAHASISWA = "create table " + TABLE_NAME +
      " (" + _ID + " integer primary key autoincrement, " +
      NAMA + " text not null, " +
      NIM + " text not null);";

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_MAHASISWA);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }
}