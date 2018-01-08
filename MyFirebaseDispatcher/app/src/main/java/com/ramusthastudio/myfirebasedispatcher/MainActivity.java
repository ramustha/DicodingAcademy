package com.ramusthastudio.myfirebasedispatcher;

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

import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CITY;
import static com.ramusthastudio.myfirebasedispatcher.MyJobService.DISPATCHER_TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button fStartBtn;
  private Button fCancelBtn;
  private FirebaseJobDispatcher fDispatcher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fStartBtn = findViewById(R.id.btn_start);
    fCancelBtn = findViewById(R.id.btn_cancel);

    fStartBtn.setOnClickListener(this);
    fCancelBtn.setOnClickListener(this);

    fDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
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
    Toast.makeText(this, "Dispatcher Created", Toast.LENGTH_SHORT).show();
    Bundle myExtrasBundle = new Bundle();
    myExtrasBundle.putString(MyJobService.EXTRAS_CITY, CITY);

    Job myJob = fDispatcher.newJobBuilder()
        // kelas service yang akan dipanggil
        .setService(MyJobService.class)
        // unique tag untuk identifikasi job
        .setTag(DISPATCHER_TAG)
        // one-off job
        // true job tersebut akan diulang, dan false job tersebut tidak diulang
        .setRecurring(true)
        // until_next_boot berarti hanya sampai next boot
        // forever berarti akan berjalan meskipun sudah reboot
        .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
        // waktu trigger 0 sampai 60 detik
        .setTrigger(Trigger.executionWindow(0, 60))
        // overwrite job dengan tag sama
        .setReplaceCurrent(true)
        // set waktu kapan akan dijalankan lagi jika gagal
        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
        // set kondisi dari device
        .setConstraints(
            // hanya berjalan saat ada koneksi yang unmetered (contoh Wifi)
            Constraint.ON_UNMETERED_NETWORK,
            // hanya berjalan ketika device di charge
            Constraint.DEVICE_CHARGING

            // berjalan saat ada koneksi internet
            //Constraint.ON_ANY_NETWORK

            // berjalan saat device dalam kondisi idle
            //Constraint.DEVICE_IDLE
        )
        .setExtras(myExtrasBundle)
        .build();

    fDispatcher.mustSchedule(myJob);
  }

  private void cancelJob() {
    Toast.makeText(this, "Dispatcher Cancelled", Toast.LENGTH_SHORT).show();
    fDispatcher.cancel(DISPATCHER_TAG);
  }
}
