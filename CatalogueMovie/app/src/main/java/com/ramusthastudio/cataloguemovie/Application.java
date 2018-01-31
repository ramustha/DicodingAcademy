package com.ramusthastudio.cataloguemovie;

import android.os.Bundle;
import android.widget.Toast;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.ramusthastudio.cataloguemovie.service.DailyReminderService;
import com.ramusthastudio.cataloguemovie.service.UpcomingReminderService;

import static com.ramusthastudio.cataloguemovie.BuildConfig.LANGUAGE;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_API;
import static com.ramusthastudio.cataloguemovie.BuildConfig.SERVER_URL;
import static com.ramusthastudio.cataloguemovie.service.DailyReminderService.DAILY_REMINDER_TAG;
import static com.ramusthastudio.cataloguemovie.service.UpcomingReminderService.UPCOMING_REMIDER_TAG;

public class Application extends android.app.Application {
  private FirebaseJobDispatcher jobDispatcher;

  @Override
  public void onCreate() {
    super.onCreate();

    jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
    startJob();
  }

  private void startJob() {
    Bundle myExtrasBundle = new Bundle();
    myExtrasBundle.putString(UpcomingReminderService.EXTRA_JOB_SERVICE, SERVER_URL + "/movie/upcoming?api_key=" + SERVER_API + "&language=" + LANGUAGE + "&sort_by=popularity.desc");

    Job upcomingJob = jobDispatcher.newJobBuilder()
        // kelas service yang akan dipanggil
        .setService(UpcomingReminderService.class)
        // unique tag untuk identifikasi job
        .setTag(UPCOMING_REMIDER_TAG)
        // one-off job
        // true job tersebut akan diulang, dan false job tersebut tidak diulang
        .setRecurring(true)
        // until_next_boot berarti hanya sampai next boot
        // forever berarti akan berjalan meskipun sudah reboot
        .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
        // .setTrigger(Trigger.executionWindow(0, 60 * 60))
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

    Job dailyReminder = jobDispatcher.newJobBuilder()
        // kelas service yang akan dipanggil
        .setService(DailyReminderService.class)
        // unique tag untuk identifikasi job
        .setTag(DAILY_REMINDER_TAG)
        // one-off job
        // true job tersebut akan diulang, dan false job tersebut tidak diulang
        .setRecurring(true)
        // until_next_boot berarti hanya sampai next boot
        // forever berarti akan berjalan meskipun sudah reboot
        .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
        // .setTrigger(Trigger.executionWindow(0, 60 * 60 * 24))
        .setTrigger(Trigger.executionWindow(0, 60))
        // overwrite job dengan tag sama
        .setReplaceCurrent(true)
        // set waktu kapan akan dijalankan lagi jika gagal
        .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
        // set kondisi dari device
        .setConstraints(
            Constraint.ON_ANY_NETWORK,
            Constraint.DEVICE_IDLE
        )
        .build();

    jobDispatcher.mustSchedule(upcomingJob);
    jobDispatcher.mustSchedule(dailyReminder);
  }

  private void cancelJob() {
    Toast.makeText(this, "Dispatcher Cancelled", Toast.LENGTH_SHORT).show();
    jobDispatcher.cancel(UPCOMING_REMIDER_TAG);
  }
}
