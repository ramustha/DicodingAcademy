package com.ramusthastudio.myfirebasedispatcher;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.MalformedJsonException;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.ramusthastudio.myfirebasedispatcher.model.OpenWeather;
import com.ramusthastudio.myfirebasedispatcher.model.Weather;
import cz.msebera.android.httpclient.Header;
import java.text.DecimalFormat;

import static com.ramusthastudio.myfirebasedispatcher.BuildConfig.SERVER_API;
import static com.ramusthastudio.myfirebasedispatcher.BuildConfig.SERVER_URL;

public class MyJobService extends JobService {
  public static final String DISPATCHER_TAG = "MyJobService";
  public static String EXTRAS_CITY = "extras_city";
  private static final String TAG = "GetWeather";
  private static final String CITY = "Bandung";
  private static final String URL = SERVER_URL + "?q=" + CITY + "&appid=" + SERVER_API;
  private static AsyncHttpClient sHttpClient = new AsyncHttpClient();
  private static DecimalFormat sDecimalFormat = new DecimalFormat("##.##");

  @Override
  public boolean onStartJob(JobParameters aJobParameters) {
    Log.d(TAG, "onStartJob() Executed");

    // return true ketika ingin dijalankan proses dengan thread yang berbeda, misal asynctask
    getCurrentWeather(aJobParameters);
    return true;
  }
  @Override
  public boolean onStopJob(JobParameters job) {
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
        // ketika proses selesai, maka perlu dipanggil jobFinished dengan parameter false;
        jobFinished(aJobParameters, false);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, OpenWeather errorResponse) {
        Log.e(TAG, "onFailure ", throwable);
        jobFinished(aJobParameters, true);
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
