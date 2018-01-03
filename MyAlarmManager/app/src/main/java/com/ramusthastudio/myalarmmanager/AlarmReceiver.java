package com.ramusthastudio.myalarmmanager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
  private static final String TYPE_ONE_TIME = "OneTimeAlarm";
  private static final String TYPE_REPEATING = "RepeatingAlarm";
  private static final String EXTRA_MESSAGE = "message";
  private static final String EXTRA_TYPE = "type";
  private static final int NOTIF_ID_ONETIME = 100;
  private static final int NOTIF_ID_REPEATING = 101;

  @Override
  public void onReceive(Context context, Intent intent) {
    String type = intent.getStringExtra(EXTRA_TYPE);
    String message = intent.getStringExtra(EXTRA_MESSAGE);
    String title = type.equalsIgnoreCase(TYPE_ONE_TIME) ? "One Time Alarm" : "Repeating Alarm";
    int notifId = type.equalsIgnoreCase(TYPE_ONE_TIME) ? NOTIF_ID_ONETIME : NOTIF_ID_REPEATING;
    showAlarmNotification(context, title, message, notifId);
  }

  private static void showAlarmNotification(Context context, String title, String message, int notifId) {
    NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder builder;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      builder = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID);
    } else {
      builder = new NotificationCompat.Builder(context);
    }
    builder
        .setSmallIcon(R.drawable.ic_alarm)
        .setContentTitle(title)
        .setContentText(message)
        .setColor(ContextCompat.getColor(context, android.R.color.transparent))
        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
        .setSound(alarmSound);

    if (notificationManagerCompat != null) {
      notificationManagerCompat.notify(notifId, builder.build());
    }
  }

  public void setOneTimeAlarm(Context context, long aDate, String message) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (alarmManager != null) {
      int requestCode = NOTIF_ID_ONETIME;
      Intent intent = new Intent(context, AlarmReceiver.class);
      intent.putExtra(EXTRA_MESSAGE, message);
      intent.putExtra(EXTRA_TYPE, TYPE_ONE_TIME);

      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
      alarmManager.set(AlarmManager.RTC_WAKEUP, aDate, pendingIntent);

      Toast.makeText(context, "One time alarm set up", Toast.LENGTH_SHORT).show();
    }
  }

  public void setRepeatingAlarm(Context context, long aDate, String message){
    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    if (alarmManager != null) {
      Intent intent = new Intent(context, AlarmReceiver.class);
      intent.putExtra(EXTRA_MESSAGE, message);
      intent.putExtra(EXTRA_TYPE, TYPE_REPEATING);
      int requestCode = NOTIF_ID_ONETIME;

      PendingIntent pendingIntent =  PendingIntent.getBroadcast(context, requestCode, intent, 0);
      alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, aDate, AlarmManager.INTERVAL_DAY, pendingIntent);

      Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show();
    }
  }

  public void cancelAlarm(Context context) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (alarmManager != null) {
      Intent intent = new Intent(context, AlarmReceiver.class);
      int requestCode = NOTIF_ID_REPEATING;
      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
      alarmManager.cancel(pendingIntent);
    }
  }
}
