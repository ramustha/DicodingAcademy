package com.ramusthastudio.myjobscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.MalformedJsonException;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.ramusthastudio.myjobscheduler.model.OpenWeather;
import com.ramusthastudio.myjobscheduler.model.Weather;
import cz.msebera.android.httpclient.Header;
import java.text.DecimalFormat;

import static com.ramusthastudio.myjobscheduler.BuildConfig.SERVER_API;
import static com.ramusthastudio.myjobscheduler.BuildConfig.SERVER_URL;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public final class GetCurrentWeatherJobService extends JobService {
  private static final String TAG = GetCurrentWeatherJobService.class.getSimpleName();
  private static final String CITY = "Bandung";
  private static final String URL = SERVER_URL + "?q=" + CITY + "&appid=" + SERVER_API;
  private static AsyncHttpClient sHttpClient = new AsyncHttpClient();
  private static DecimalFormat sDecimalFormat = new DecimalFormat("##.##");

  @Override
  public boolean onStartJob(JobParameters params) {
    Log.d(TAG, "onStartJob() Executed");
    getCurrentWeather(params);

    return true;
  }

  @Override
  public boolean onStopJob(JobParameters params) {
    Log.d(TAG, "onStopJob() Executed");

    return true;
  }

  private void getCurrentWeather(final JobParameters aJobParameters) {
    Log.d(TAG, "Running, getCurrentWeather: " + URL);

    sHttpClient.get(URL, new BaseJsonHttpResponseHandler<OpenWeather>() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, OpenWeather response) {
        Log.d(TAG, "Weather " + response);

        if (response == null) {
          throw new NullPointerException("Weater cant be null");
        }
        int notifId = 100;

        StringBuilder b = new StringBuilder();
        for (Weather weather : response.getWeather()) {
          b.append(weather.getMain()).append(", ").append(weather.getDescription()).append("\n");
        }
        b.append("Temp: ").append(sDecimalFormat.format(response.getMain().getTemp()));

        showNotification(getApplicationContext(), "Current Weather", b.toString(), notifId);
        jobFinished(aJobParameters, false);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, OpenWeather errorResponse) {
        // ketika proses gagal, maka jobFinished diset dengan parameter true. Yang artinya job perlu di reschedule
        jobFinished(aJobParameters, true);
        Log.e(TAG, "onFailure ", throwable);
      }

      @Override
      protected OpenWeather parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
        Log.d(TAG, "parseResponse " + rawJsonData + " isFailure " + isFailure);
        if (isFailure) {
          throw new MalformedJsonException("Error parse OpenWeather format");
        }
        return new Gson().fromJson(rawJsonData, OpenWeather.class);
      }
    });
  }

  private static void showNotification(Context context, String title, String message, int notifId) {
    NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    NotificationCompat.Builder builder;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      builder = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID);
    } else {
      builder = new NotificationCompat.Builder(context);
    }

    builder.setContentTitle(title)
        .setSmallIcon(R.drawable.ic_cloud)
        .setContentText(message)
        .setColor(ContextCompat.getColor(context, android.R.color.white))
        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
        .setSound(alarmSound);

    if (notificationManagerCompat != null) {
      notificationManagerCompat.notify(notifId, builder.build());
    }
  }
}
