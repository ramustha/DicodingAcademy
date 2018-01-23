package com.ramusthastudio.kamus.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.ramusthastudio.kamus.model.Kamus;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.kamus.repository.KamusDatabaseContract.KamusColumn.DESCRIPTION;
import static com.ramusthastudio.kamus.repository.KamusDatabaseContract.KamusColumn.WORD;
import static com.ramusthastudio.kamus.repository.KamusDatabaseContract.TABLE_ENGLISH_INDONESIA;
import static com.ramusthastudio.kamus.repository.KamusDatabaseContract.TABLE_INDONESIA_ENGLISH;

public final class RepositoryService {
  private static SQLiteDatabase sDatabase;

  private RepositoryService() {}

  public static void init(Context aContext) {
    KamusDatabaseHelper helper = new KamusDatabaseHelper(aContext);
    sDatabase = helper.getWritableDatabase();
  }

  public static void beginTransaction() {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    sDatabase.beginTransaction();
  }

  public static void setTransactionSuccessful() {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    sDatabase.setTransactionSuccessful();
  }

  public static void endTransaction() {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    sDatabase.endTransaction();
  }

  public static void close() {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }
    sDatabase.close();
  }

  public static List<Kamus> getAllEnglishIndonesiaData() {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    Cursor cursor = sDatabase.query(
        TABLE_ENGLISH_INDONESIA, null, null, null, null, null, _ID + " ASC", null);
    return addData(cursor);
  }

  public static List<Kamus> getAllIndonesiaEnglishData() {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    Cursor cursor = sDatabase.query(
        TABLE_INDONESIA_ENGLISH, null, null, null, null, null, _ID + " ASC", null);
    return addData(cursor);
  }

  public static void insertEnglishIndonesia(Kamus aKamus) {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    String sql = "INSERT INTO " + TABLE_ENGLISH_INDONESIA + " (" + WORD + ", " + DESCRIPTION + ") VALUES (?, ?)";
    SQLiteStatement stmt = sDatabase.compileStatement(sql);
    stmt.bindString(1, aKamus.getWord());
    stmt.bindString(2, aKamus.getDescription());
    stmt.execute();
    stmt.clearBindings();
  }

  public static void insertIndonesiaEnglish(Kamus aKamus) {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    String sql = "INSERT INTO " + TABLE_INDONESIA_ENGLISH + " (" + WORD + ", " + DESCRIPTION + ") VALUES (?, ?)";
    SQLiteStatement stmt = sDatabase.compileStatement(sql);
    stmt.bindString(1, aKamus.getWord());
    stmt.bindString(2, aKamus.getDescription());
    stmt.execute();
    stmt.clearBindings();
  }

  public static List<Kamus> getIndonesiaEnglishByWordLike(String nama) {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    Cursor cursor = sDatabase.query(
        TABLE_INDONESIA_ENGLISH, null, WORD + " LIKE ?", new String[] {"%" + nama + "%"}, null, null, _ID + " ASC", null);
    return addData(cursor);
  }

  public static List<Kamus> getEnglishIndonesiaByWordLike(String nama) {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    Cursor cursor = sDatabase.query(
        TABLE_ENGLISH_INDONESIA, null, WORD + " LIKE ?", new String[] {"%" + nama + "%"}, null, null, _ID + " ASC", null);
    return addData(cursor);
  }

  public static List<Kamus> getIndonesiaEnglishByWord(String nama) {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    Cursor cursor = sDatabase.query(
        TABLE_INDONESIA_ENGLISH, null, WORD + " LIKE ?", new String[] {nama}, null, null, _ID + " ASC", null);
    return addData(cursor);
  }

  public static List<Kamus> getEnglishIndonesiaByWord(String nama) {
    if (sDatabase == null) {
      throw new NullPointerException("Call KamusHelper.init() first");
    }

    Cursor cursor = sDatabase.query(
        TABLE_ENGLISH_INDONESIA, null, WORD + " LIKE ?", new String[] {nama}, null, null, _ID + " ASC", null);
    return addData(cursor);
  }

  private static List<Kamus> addData(final Cursor aCursor) {
    aCursor.moveToFirst();
    List<Kamus> kamusList = new ArrayList<>();
    if (aCursor.getCount() > 0) {
      do {
        kamusList.add(new Kamus(
            aCursor.getInt(aCursor.getColumnIndexOrThrow(_ID)),
            aCursor.getString(aCursor.getColumnIndexOrThrow(WORD)),
            aCursor.getString(aCursor.getColumnIndexOrThrow(DESCRIPTION))
        ));
        aCursor.moveToNext();
      } while (!aCursor.isAfterLast());
    }
    aCursor.close();
    return kamusList;
  }
}
