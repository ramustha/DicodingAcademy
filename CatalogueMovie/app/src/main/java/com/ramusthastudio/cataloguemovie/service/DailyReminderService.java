package com.ramusthastudio.cataloguemovie.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.ramusthastudio.cataloguemovie.MoviesActivity;
import com.ramusthastudio.cataloguemovie.R;

public final class DailyReminderService extends JobService {
  private static final int DAILY_CODE = 111;
  public static final String DAILY_REMINDER_TAG = "DailyReminderService";
  public static final String CHANNEL_ID = "channel_id";

  public DailyReminderService() { }

  @Override
  public boolean onStartJob(JobParameters aJobParameters) {
    Log.d(DailyReminderService.class.getSimpleName(), "DailyReminderService Executed");
    showNotification(getApplicationContext(), 27);
    jobFinished(aJobParameters, false);
    return true;
  }

  @Override
  public boolean onStopJob(JobParameters job) {
    Log.d(DailyReminderService.class.getSimpleName(), "DailyReminderService Executed");
    return true;
  }

  private static void showNotification(final Context context, int notifId) {
    NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    Intent notificationIntent = new Intent(context, MoviesActivity.class);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
    final PendingIntent intent = PendingIntent.getActivity(context, DAILY_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    builder
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(context.getString(R.string.daily_remider_text))
        .setContentIntent(intent)
        // .setAutoCancel(true)
        .setSound(alarmSound);

    if (notificationManagerCompat != null) {
      notificationManagerCompat.notify(notifId, builder.build());
    }
  }
}
