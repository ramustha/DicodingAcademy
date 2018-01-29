package com.ramusthastudio.mywidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.util.Log;
import android.widget.RemoteViews;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class UpdateWidgetService extends JobService {

  @Override
  public boolean onStartJob(JobParameters jobParameters) {
    Log.d("UpdateWidgetService", "onStartJob");

    AppWidgetManager manager = AppWidgetManager.getInstance(this);

    RemoteViews view = new RemoteViews(getPackageName(), R.layout.random_number_widget);
    ComponentName theWidget = new ComponentName(this, RandomNumberWidget.class);

    String lastUpdate = "Random: " + NumberGenerator.Generate(100);
    view.setTextViewText(R.id.appwidget_text, lastUpdate);

    manager.updateAppWidget(theWidget, view);

    jobFinished(jobParameters, false);
    return true;
  }

  @Override
  public boolean onStopJob(JobParameters jobParameters) {
    return false;
  }
}
