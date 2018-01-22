package com.ramusthastudio.mynotesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.mynotesapp.DatabaseContract.NoteColumns.DATE;
import static com.ramusthastudio.mynotesapp.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.ramusthastudio.mynotesapp.DatabaseContract.NoteColumns.TITLE;
import static com.ramusthastudio.mynotesapp.DatabaseContract.TABLE_NOTE;

public final class NoteHelper {
  private static final String DATABASE_TABLE = TABLE_NOTE;
  private final Context context;
  private DatabaseHelper dataBaseHelper;
  private SQLiteDatabase database;

  public NoteHelper(Context context) {
    this.context = context;
  }

  public NoteHelper open() throws SQLException {
    dataBaseHelper = new DatabaseHelper(context);
    database = dataBaseHelper.getWritableDatabase();
    return this;
  }

  public void close() {
    dataBaseHelper.close();
  }

  public List<Note> query() {
    List<Note> arrayList = new ArrayList<>();
    Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " DESC", null);
    cursor.moveToFirst();
    Note note;
    if (cursor.getCount() > 0) {
      do {
        note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
        note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
        note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

        arrayList.add(note);
        cursor.moveToNext();

      } while (!cursor.isAfterLast());
    }
    cursor.close();
    return arrayList;
  }

  public long insert(Note note) {
    ContentValues args = new ContentValues();
    args.put(TITLE, note.getTitle());
    args.put(DESCRIPTION, note.getDescription());
    args.put(DATE, note.getDate());
    return database.insert(DATABASE_TABLE, null, args);
  }

  public int update(Note note) {
    ContentValues args = new ContentValues();
    args.put(TITLE, note.getTitle());
    args.put(DESCRIPTION, note.getDescription());
    args.put(DATE, note.getDate());
    return database.update(DATABASE_TABLE, args, _ID + "= '" + note.getId() + "'", null);
  }

  public int delete(int id) {
    return database.delete(TABLE_NOTE, _ID + " = '" + id + "'", null);
  }
}
