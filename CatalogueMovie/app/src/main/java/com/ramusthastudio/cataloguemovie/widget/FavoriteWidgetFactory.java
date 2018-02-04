package com.ramusthastudio.cataloguemovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ramusthastudio.cataloguemovie.BuildConfig;
import com.ramusthastudio.cataloguemovie.R;
import com.ramusthastudio.cataloguemovie.model.Widget;
import com.ramusthastudio.cataloguemovie.repo.DatabaseContract;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.CONTENT_URI;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.BACKDROP;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.RATING;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.ramusthastudio.cataloguemovie.repo.DatabaseContract.MovieColumns.TITLE;

public final class FavoriteWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
  private final List<Widget> fWidgetItems = new ArrayList<>();
  private final Context fContext;
  private final Intent fIntent;
  private int fWidgetId;

  public FavoriteWidgetFactory(final Context aContext, final Intent aIntent) {
    fContext = aContext;
    fIntent = aIntent;
    fWidgetId = fIntent
        .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
  }

  @Override public int getCount() { return fWidgetItems.size(); }
  @Override public int getViewTypeCount() { return 1; }
  @Override public long getItemId(final int position) { return fWidgetItems.get(position).getId(); }
  @Override public boolean hasStableIds() { return true; }
  @Override public RemoteViews getLoadingView() { return null; }

  @Override
  public void onCreate() {
    fillDataFromResolver();

    // We sleep for 3 seconds here to show how the empty view appears in the interim.
    try {
      Thread.sleep(3000);
    } catch (InterruptedException ignored) { }
  }

  @Override
  public RemoteViews getViewAt(final int position) {
    final Bitmap poster = fWidgetItems.get(position).getBackdropPath();
    final String title = fWidgetItems.get(position).getTitle() + "(" + fWidgetItems.get(position).getVoteAverage() + ")";

    RemoteViews remoteViews = new RemoteViews(fContext.getPackageName(), R.layout.favorite_movie_widget_item);
    remoteViews.setImageViewBitmap(R.id.widget_item_image_view, poster);
    remoteViews.setTextViewText(R.id.widget_item_title_view, title);

    Bundle extras = new Bundle();
    extras.putInt(FavoriteWidget.EXTRA_ID, fWidgetItems.get(position).getId());
    Intent fillInIntent = new Intent();
    fillInIntent.putExtras(extras);

    remoteViews.setOnClickFillInIntent(R.id.widget_item_image_view, fillInIntent);
    return remoteViews;
  }

  @Override
  public void onDataSetChanged() {
    Log.d("Favorite Widget", "onDataSetChanged");
    // final long token = Binder.clearCallingIdentity();
    // try {
    //   fillDataFromResolver();
    // } finally {
    //   Binder.restoreCallingIdentity(token);
    // }
  }

  @Override
  public void onDestroy() {
    fWidgetItems.clear();
  }

  private void fillDataFromResolver() {
    final ContentResolver resolver = fContext.getContentResolver();
    final Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
    if (cursor != null) {
      while (cursor.moveToNext()) {
        final int dbId = DatabaseContract.getColumnInt(cursor, _ID);
        final String title = DatabaseContract.getColumnString(cursor, TITLE);
        final String backdrop = DatabaseContract.getColumnString(cursor, BACKDROP);
        final long releaseDate = DatabaseContract.getColumnLong(cursor, RELEASE_DATE);
        final double rating = DatabaseContract.getColumnDouble(cursor, RATING);

        final String imageUrl = BuildConfig.IMAGE_URL + "/w500" + backdrop;
        Glide
            .with(fContext)
            .asBitmap()
            .load(imageUrl)
            .into(new SimpleTarget<Bitmap>() {
              @Override
              public void onResourceReady(
                  @NonNull final Bitmap resource,
                  @Nullable final Transition<? super Bitmap> transition) {
                fWidgetItems.add(new Widget(
                    dbId,
                    title,
                    rating,
                    resource,
                    new Date(releaseDate)
                ));

              }
            });
      }
      cursor.close();
    }
  }

}
