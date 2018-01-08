package com.ramusthastudio.myjobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button fStartBtn;
  private Button fCancelBtn;
  private static final int JOB_ID = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fStartBtn = findViewById(R.id.btn_start);
    fCancelBtn = findViewById(R.id.btn_cancel);

    fStartBtn.setOnClickListener(this);
    fCancelBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_start:
        startJob();
        break;
      case R.id.btn_cancel:
        cancelJob();
        break;
    }
  }

  private void startJob() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      ComponentName mServiceComponent = new ComponentName(this, GetCurrentWeatherJobService.class);
      JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, mServiceComponent);
      builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
      builder.setRequiresDeviceIdle(false);
      builder.setRequiresCharging(false);

      // 1000 ms = 1 detik
      builder.setPeriodic(18000);
      JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
      if (jobScheduler != null) {
        jobScheduler.schedule(builder.build());
        Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void cancelJob() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
      if (jobScheduler != null) {
        jobScheduler.cancel(JOB_ID);
        Toast.makeText(this, "Job Service canceled", Toast.LENGTH_SHORT).show();
        finish();
      }
    }
  }
}
