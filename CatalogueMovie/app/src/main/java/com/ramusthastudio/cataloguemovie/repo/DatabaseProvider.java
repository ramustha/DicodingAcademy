package com.ramusthastudio.cataloguemovie.repo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.AUTHORITY;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.CONTENT_URI;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.MOVIE_ID;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.TABLE_NAME;

public class DatabaseProvider extends ContentProvider {
  private static final int MOVIE = 1;
  private static final int MOVIE_PATH_ID = 2;
  private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  private SQLiteDatabase database;

  static {
    // content://com.ramusthastudio.cataloguemovie/moviedb
    sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);

    // content://com.ramusthastudio.cataloguemovie/moviedb/id
    sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_PATH_ID);
  }

  @Override
  public boolean onCreate() {
    DatabaseHelper helper = new DatabaseHelper(getContext());
    database = helper.getWritableDatabase();
    return database.isOpen();
  }

  @Override
  public @Nullable Cursor query(
      @NonNull final Uri uri,
      @Nullable final String[] projection,
      @Nullable final String selection,
      @Nullable final String[] selectionArgs,
      @Nullable final String sortOrder) {
    Cursor cursor;
    switch (sUriMatcher.match(uri)) {
      case MOVIE:
        cursor = database.query(TABLE_NAME
            , null
            , null
            , null
            , null
            , null
            , RELEASE_DATE + " DESC");
        break;
      case MOVIE_PATH_ID:
        cursor = database.query(TABLE_NAME
            , null
            , MOVIE_ID + " = ?"
            , new String[] {uri.getLastPathSegment()}
            , null
            , null
            , null
            , null);
        break;
      default:
        cursor = null;
        break;
    }

    if (cursor != null) {
      if (getContext() != null) {
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
      }
    }

    return cursor;
  }

  @Override public @Nullable String getType(@NonNull final Uri uri) { return null; }

  @Override
  public @Nullable Uri insert(@NonNull final Uri uri, @Nullable final ContentValues values) {
    long added;
    switch (sUriMatcher.match(uri)) {
      case MOVIE:
        added = database.insert(TABLE_NAME, null, values);
        break;
      default:
        added = 0;
        break;
    }

    if (added > 0) {
      if (getContext() != null) {
        getContext().getContentResolver().notifyChange(uri, null);
      }
    }
    return Uri.parse(CONTENT_URI + "/" + added);
  }

  @Override
  public int update(
      @NonNull final Uri uri,
      @Nullable final ContentValues values,
      @Nullable final String selection,
      @Nullable final String[] selectionArgs) {
    int updated;
    switch (sUriMatcher.match(uri)) {
      case MOVIE_PATH_ID:
        updated = database.update(TABLE_NAME, values, MOVIE_ID + " = ?", new String[] {uri.getLastPathSegment()});
        break;
      default:
        updated = 0;
        break;
    }

    if (updated > 0) {
      if (getContext() != null) {
        getContext().getContentResolver().notifyChange(uri, null);
      }
    }
    return updated;
  }

  @Override
  public int delete(
      @NonNull final Uri uri,
      @Nullable final String selection,
      @Nullable final String[] selectionArgs) {
    int deleted;
    switch (sUriMatcher.match(uri)) {
      case MOVIE_PATH_ID:
        deleted = database.delete(TABLE_NAME, MOVIE_ID + " = ?", new String[] {uri.getLastPathSegment()});
        break;
      default:
        deleted = 0;
        break;
    }

    if (deleted > 0) {
      if (getContext() != null) {
        getContext().getContentResolver().notifyChange(uri, null);
      }
    }

    return deleted;
  }
}
