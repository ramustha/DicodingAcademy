package com.ramusthastudio.mynotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import static com.ramusthastudio.mynotification.NotificationService.REPLY_ACTION;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
  private static final String CHANNEL_ID = "channel_id";
  private static final String KEY_NOTIFICATION_ID = "key_noticiation_id";
  private static final String KEY_MESSAGE_ID = "key_message_id";

  public NotificationBroadcastReceiver() { }

  public static Intent getReplyMessageIntent(Context context, String channelId, int notificationId, int messageId) {
    Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
    intent.setAction(REPLY_ACTION);
    intent.putExtra(CHANNEL_ID, channelId);
    intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
    intent.putExtra(KEY_MESSAGE_ID, messageId);
    return intent;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (REPLY_ACTION.equals(intent.getAction())) {
      CharSequence message = NotificationService.getReplyMessage(intent);
      int messageId = intent.getIntExtra(KEY_MESSAGE_ID, 0);

      Toast.makeText(context, "Message ID: " + messageId + "\nMessage: " + message, Toast.LENGTH_SHORT).show();

      int notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 1);
      String channelId = intent.getStringExtra(CHANNEL_ID);
      updateNotification(context, channelId, notifyId);
    }
  }

  private static void updateNotification(Context context, final String aChannelId, int notifyId) {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, aChannelId)
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle(context.getString(R.string.notif_title_sent))
        .setContentText(context.getString(R.string.notif_content_sent));
    notificationManager.notify(notifyId, builder.build());
  }
}
