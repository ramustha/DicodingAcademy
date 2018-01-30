package com.ramusthastudio.myfirebasecloudmessaging;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MessagingInstanceIdService extends FirebaseInstanceIdService {
  private static final String TAG = "InstanceIdService";

  @Override
  public void onTokenRefresh() {
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);
    sendRegistrationToServer(refreshedToken);
  }

  private void sendRegistrationToServer(String token) {
    // TODO: Implement this method to send token to your app server.
  }
}
