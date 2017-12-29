package com.ramusthastudio.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class OriginService extends Service {
  public static final String ORIGIN_SERVICE = "OriginService";
  private volatile ServiceHandler fServiceHandler;

  public OriginService() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    HandlerThread thread = new HandlerThread("OriginService");
    thread.start();

    fServiceHandler = new ServiceHandler(thread.getLooper());
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(ORIGIN_SERVICE, "OriginService dijalankan");
    Message msg = fServiceHandler.obtainMessage();
    msg.arg1 = startId;
    msg.obj = intent;
    fServiceHandler.sendMessage(msg);
    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(ORIGIN_SERVICE, "onDestroy()");
  }

  private final class ServiceHandler extends Handler {
    public ServiceHandler(Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
      if (msg.obj != null) {
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }

        Log.d(ORIGIN_SERVICE, "StopService");
        stopSelf(msg.arg1);
      }
    }
  }
}
