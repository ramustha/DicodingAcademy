package com.ramusthastudio.mynotification;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

public class NotificationService extends IntentService {
  public static final String REPLY_ACTION = "com.ramusthastudio.mynotification.directreply.REPLY_ACTION";
  private static final String CHANNEL_ID = "channel_notification";
  private static final String KEY_REPLY = "key_reply_message";
  private static final int NOTIFICATION_ID = 1;
  private static final int MESSAGE_ID = 123;

  public NotificationService() {
    super("NotificationService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      showNotification();
    }
  }

  private void showNotification() {
    String replyLabel = getString(R.string.notif_action_reply);
    RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY)
        .setLabel(replyLabel)
        .build();

    NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
        android.R.drawable.ic_menu_rotate, replyLabel, getReplyPendingIntent())
        .addRemoteInput(remoteInput)
        .setAllowGeneratedReplies(true)
        .build();

    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle(getString(R.string.notif_title))
        .setContentText(getString(R.string.notif_content))
        .setShowWhen(true)
        .addAction(replyAction);

    NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
  }

  private PendingIntent getReplyPendingIntent() {
    Intent intent;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      intent = NotificationBroadcastReceiver.getReplyMessageIntent(this, CHANNEL_ID, NOTIFICATION_ID, MESSAGE_ID);
      return PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    } else {
      intent = ReplyActivity.getReplyMessageIntent(this, CHANNEL_ID, NOTIFICATION_ID, MESSAGE_ID);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      return PendingIntent.getActivity(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
  }

  public static CharSequence getReplyMessage(Intent intent) {
    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
    if (remoteInput != null) {
      return remoteInput.getCharSequence(KEY_REPLY);
    }
    return null;
  }
}
