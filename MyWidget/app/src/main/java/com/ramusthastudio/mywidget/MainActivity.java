package com.ramusthastudio.mywidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button btnStart;
  private Button btnStop;
  private FirebaseJobDispatcher jobDispatcher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnStart = findViewById(R.id.btn_start);
    btnStop = findViewById(R.id.btn_stop);

    btnStart.setOnClickListener(this);
    btnStop.setOnClickListener(this);
    jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
  }

  @Override
  public void onClick(View view) {

    int id = view.getId();
    switch (id) {
      case R.id.btn_start:
        startJob();
        break;

      case R.id.btn_stop:
        cancelJob();
        break;
    }

  }

  private static final String JOBID = "widget_jobs";

  private void startJob() {
    Job myJob = jobDispatcher.newJobBuilder()
        // the JobService that will be called
        .setService(UpdateWidgetService.class)
        // uniquely identifies the job
        .setTag(JOBID)
        // one-off job
        .setRecurring(false)
        // don't persist past a device reboot
        .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
        // start between 0 and 60 seconds from now
        .setTrigger(Trigger.executionWindow(0, 60))
        // don't overwrite an existing job with the same tag
        .setReplaceCurrent(false)
        // retry with exponential backoff
        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
        // constraints that need to be satisfied for the job to run
        .setConstraints(
            // only run on an unmetered network
            Constraint.ON_UNMETERED_NETWORK,
            // only run when the device is charging
            Constraint.DEVICE_CHARGING
        )
        .build();

    jobDispatcher.mustSchedule(myJob);

    Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show();
  }

  private void cancelJob() {
    jobDispatcher.cancel(JOBID);

    Toast.makeText(this, "Job Service canceled", Toast.LENGTH_SHORT).show();
  }
}
