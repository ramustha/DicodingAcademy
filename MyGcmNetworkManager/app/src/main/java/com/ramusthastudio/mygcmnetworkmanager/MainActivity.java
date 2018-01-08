package com.ramusthastudio.mygcmnetworkmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button fStartBtn;
  private Button fCancelBtn;
  private SchedulerTask fSchedulerTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fStartBtn = findViewById(R.id.btn_start);
    fCancelBtn = findViewById(R.id.btn_cancel);

    fStartBtn.setOnClickListener(this);
    fCancelBtn.setOnClickListener(this);
    fSchedulerTask = new SchedulerTask(this);
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
    fSchedulerTask.createPeriodicTask();
    Toast.makeText(this, "Periodic Task Created", Toast.LENGTH_SHORT).show();
  }

  private void cancelJob() {
    fSchedulerTask.cancelPeriodicTask();
    Toast.makeText(this, "Periodic Task Cancelled", Toast.LENGTH_SHORT).show();
  }
}
