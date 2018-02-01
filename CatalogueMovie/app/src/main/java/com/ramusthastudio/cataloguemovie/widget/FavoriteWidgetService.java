package com.ramusthastudio.cataloguemovie.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public final class FavoriteWidgetService extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(final Intent intent) {
    return new FavoriteWidgetFactory(getApplicationContext(), intent);
  }
}
