package com.ramusthastudio.cataloguemovie;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.ramusthastudio.cataloguemovie.model.Moviedb;
import com.ramusthastudio.cataloguemovie.model.Result;

public final class MovieService extends JobService implements Tasks.TaskListener<Moviedb> {
  public static final String DISPATCHER_TAG = "MovieService";
  public static final String EXTRA_JOB_SERVICE = "extra_job_service";
  private final Tasks<Moviedb> fTasks;

  public MovieService() {
    fTasks = new Tasks<>(this);
  }

  @Override
  public boolean onStartJob(JobParameters aJobParameters) {
    Log.d(MovieService.class.getSimpleName(), "onStartJob() Executed");
    fTasks.start(aJobParameters);
    return true;
  }

  @Override public boolean onStopJob(JobParameters job) {
    Log.d(MovieService.class.getSimpleName(), "onStartJob() Executed");
    return true;
  }

  @Override
  public void onSuccess(Moviedb aResponse, JobParameters aJobParameters) {
    Log.d(MainActivity.class.getSimpleName(), aResponse.toString());
    showNotification(getApplicationContext(), aResponse, 100);
    jobFinished(aJobParameters, false);
  }

  @Override
  public void onFailure(int statusCode, Throwable aThrowable, JobParameters aJobParameters) {
    Log.d(MainActivity.class.getSimpleName(), String.format("status %s, couse %s", statusCode, aThrowable));
    jobFinished(aJobParameters, false);
  }

  @Override public Class<Moviedb> toClass() { return Moviedb.class; }

  private static void showNotification(Context context, Moviedb aMoviedb, int notifId) {
    NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    NotificationCompat.Builder builder;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      builder = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID);
    } else {
      builder = new NotificationCompat.Builder(context);
    }

    //first index
    Result movie = aMoviedb.getResults().get(0);

    builder.setContentTitle(movie.getTitle())
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentText(movie.getOverview())
        .setColor(ContextCompat.getColor(context, android.R.color.white))
        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
        .setSound(alarmSound);

    if (notificationManagerCompat != null) {
      notificationManagerCompat.notify(notifId, builder.build());
    }
  }
}
