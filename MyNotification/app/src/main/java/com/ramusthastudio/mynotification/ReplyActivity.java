package com.ramusthastudio.mynotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.ramusthastudio.mynotification.NotificationService.REPLY_ACTION;

public class ReplyActivity extends AppCompatActivity {
  private static final String CHANNEL_ID = "channel_id";
  private static final String KEY_NOTIFICATION_ID = "key_noticiation_id";
  private static final String KEY_MESSAGE_ID = "key_message_id";
  private String mChannelId;
  private int mMessageId;
  private int mNotifyId;
  private EditText mEditReply;

  public static Intent getReplyMessageIntent(Context context, String channelId, int notificationId, int messageId) {
    Intent intent = new Intent(context, ReplyActivity.class);
    intent.setAction(REPLY_ACTION);
    intent.putExtra(CHANNEL_ID, channelId);
    intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
    intent.putExtra(KEY_MESSAGE_ID, messageId);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reply);
    Intent intent = getIntent();

    if (REPLY_ACTION.equals(intent.getAction())) {
      mMessageId = intent.getIntExtra(KEY_MESSAGE_ID, 0);
      mNotifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);
      mChannelId = intent.getStringExtra(CHANNEL_ID);
    }
    mEditReply = findViewById(R.id.edit_reply);
    ImageButton sendButton = findViewById(R.id.button_send);

    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sendMessage(mChannelId, mNotifyId, mMessageId);
      }
    });
  }

  private void sendMessage(final String aChannelId, int notifyId, int messageId) {
    updateNotification(aChannelId, notifyId);
    String message = mEditReply.getText().toString().trim();
    Toast.makeText(this, "Message ID: " + messageId + "\nMessage: " + message, Toast.LENGTH_SHORT).show();
    finish();
  }

  private void updateNotification(final String aChannelId, int notifyId) {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, aChannelId)
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle(getString(R.string.notif_title_sent))
        .setContentText(getString(R.string.notif_content_sent));
    notificationManager.notify(notifyId, builder.build());
  }
}
