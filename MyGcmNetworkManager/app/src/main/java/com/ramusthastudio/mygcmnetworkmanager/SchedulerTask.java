package com.ramusthastudio.mygcmnetworkmanager;

import android.content.Context;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

public class SchedulerTask {
  private final GcmNetworkManager fGcmNetworkManager;

  public SchedulerTask(Context context) {
    fGcmNetworkManager = GcmNetworkManager.getInstance(context);
  }

  public void createPeriodicTask() {
    Task periodicTask = new PeriodicTask.Builder()
        .setService(SchedulerService.class)
        .setPeriod(60)
        .setFlex(10)
        .setTag(SchedulerService.TAG_TASK_WEATHER_LOG)
        .setPersisted(true)
        .build();
    fGcmNetworkManager.schedule(periodicTask);
  }

  public void cancelPeriodicTask() {
    if (fGcmNetworkManager != null) {
      fGcmNetworkManager.cancelTask(SchedulerService.TAG_TASK_WEATHER_LOG, SchedulerService.class);
    }
  }
}
