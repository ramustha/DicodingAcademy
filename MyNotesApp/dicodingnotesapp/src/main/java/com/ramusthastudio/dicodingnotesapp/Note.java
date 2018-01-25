package com.ramusthastudio.dicodingnotesapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.ramusthastudio.dicodingnotesapp.DatabaseContract.getColumnInt;
import static com.ramusthastudio.dicodingnotesapp.DatabaseContract.getColumnString;

public class Note implements Parcelable {
  private int id;
  private String title;
  private String description;
  private String date;

  public Note() {
  }

  protected Note(Parcel in) {
    this.id = in.readInt();
    this.title = in.readString();
    this.description = in.readString();
    this.date = in.readString();
  }

  public Note(Cursor cursor) {
    this.id = getColumnInt(cursor, _ID);
    this.title = getColumnString(cursor, DatabaseContract.NoteColumns.TITLE);
    this.description = getColumnString(cursor, DatabaseContract.NoteColumns.DESCRIPTION);
    this.date = getColumnString(cursor, DatabaseContract.NoteColumns.DATE);
  }

  public int getId() { return id; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public String getDate() { return date; }

  public void setId(int id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.title);
    dest.writeString(this.description);
    dest.writeString(this.date);
  }

  public static final Creator<Note> CREATOR = new Creator<Note>() {

    @Override
    public Note createFromParcel(Parcel source) {
      return new Note(source);
    }

    @Override
    public Note[] newArray(int size) {
      return new Note[size];
    }
  };
}
