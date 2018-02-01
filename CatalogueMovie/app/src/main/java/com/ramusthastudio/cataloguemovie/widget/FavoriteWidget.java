package com.ramusthastudio.cataloguemovie.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import com.ramusthastudio.cataloguemovie.MoviesActivity;
import com.ramusthastudio.cataloguemovie.R;

public final class FavoriteWidget extends AppWidgetProvider {
  public static final String CLICK_ACTION = "com.ramusthastudio.cataloguemovie.CLICK_ACTION";
  public static final String EXTRA_ID = "com.ramusthastudio.cataloguemovie.EXTRA_ID";

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (CLICK_ACTION.equals(intent.getAction())) {
      int viewId = intent.getIntExtra(EXTRA_ID, 0);
      final Intent dataIntent = new Intent(context, MoviesActivity.class);
      dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      dataIntent.putExtra(CLICK_ACTION, viewId);
      context.startActivity(dataIntent);
      Log.d("Favorite Widget", "onReceive");
    }
    super.onReceive(context, intent);
  }

  private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    Intent intent = new Intent(context, FavoriteWidgetService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
    remoteViews.setRemoteAdapter(appWidgetId, R.id.widget_stack_image, intent);
    remoteViews.setEmptyView(R.id.widget_stack_image, R.id.widget_item_empty_view);

    Intent clickIntent = new Intent(context, FavoriteWidget.class);
    clickIntent.setAction(CLICK_ACTION);
    clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));

    PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setPendingIntentTemplate(R.id.widget_stack_image, toastPendingIntent);
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
  }
}
