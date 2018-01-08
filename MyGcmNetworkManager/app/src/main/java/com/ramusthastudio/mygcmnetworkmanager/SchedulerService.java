package com.ramusthastudio.mygcmnetworkmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.MalformedJsonException;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.ramusthastudio.mygcmnetworkmanager.model.OpenWeather;
import com.ramusthastudio.mygcmnetworkmanager.model.Weather;
import cz.msebera.android.httpclient.Header;
import java.text.DecimalFormat;

import static com.ramusthastudio.mygcmnetworkmanager.BuildConfig.SERVER_API;
import static com.ramusthastudio.mygcmnetworkmanager.BuildConfig.SERVER_URL;

public class SchedulerService extends GcmTaskService {
  public static final String TAG_TASK_WEATHER_LOG = "WeatherTask";
  private static final String TAG = "GetWeather";
  private static final String CITY = "Bandung";
  private static final String URL = SERVER_URL + "?q=" + CITY + "&appid=" + SERVER_API;
  private static SyncHttpClient sHttpClient = new SyncHttpClient();
  private static DecimalFormat sDecimalFormat = new DecimalFormat("##.##");

  @Override
  public void onInitializeTasks() {
    super.onInitializeTasks();
    SchedulerTask mSchedulerTask = new SchedulerTask(this);
    mSchedulerTask.createPeriodicTask();
  }

  @Override
  public int onRunTask(TaskParams aTaskParams) {
    int result = 0;
    if (aTaskParams.getTag().equals(TAG_TASK_WEATHER_LOG)) {
      getCurrentWeather();
      result = GcmNetworkManager.RESULT_SUCCESS;
    }
    return result;
  }

  private void getCurrentWeather() {
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
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, OpenWeather errorResponse) {
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
