package com.ramusthastudio.mypreloaddata;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    progressBar = findViewById(R.id.progress_bar);

    new LoadData().execute();
  }

  private class LoadData extends AsyncTask<Void, Integer, Void> {
    final String TAG = LoadData.class.getSimpleName();
    MahasiswaHelper mahasiswaHelper;
    AppPreference appPreference;
    double progress;
    double maxprogress = 100;

    @Override
    protected void onPreExecute() {
      mahasiswaHelper = new MahasiswaHelper(MainActivity.this);
      appPreference = new AppPreference(MainActivity.this);
    }

    @Override
    protected Void doInBackground(Void... params) {
      Boolean firstRun = appPreference.getFirstRun();

      if (firstRun) {
        List<MahasiswaModel> mahasiswaModels = preLoadRaw();

        mahasiswaHelper.open();

        progress = 30;
        publishProgress((int) progress);
        Double progressMaxInsert = 80.0;
        Double progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size();

        mahasiswaHelper.beginTransaction();

        try {
          for (MahasiswaModel model : mahasiswaModels) {
            mahasiswaHelper.insertTransaction(model);
            progress += progressDiff;
            publishProgress((int) progress);
          }
          // Jika semua proses telah di set success maka akan di commit ke database
          mahasiswaHelper.setTransactionSuccess();
        } catch (Exception e) {
          // Jika gagal maka do nothing
          Log.e(TAG, "doInBackground: Exception");
        }

        mahasiswaHelper.endTransaction();

        mahasiswaHelper.close();

        appPreference.setFirstRun(false);

        publishProgress((int) maxprogress);

      } else {
        try {
          synchronized (this) {
            this.wait(2000);

            publishProgress(50);

            this.wait(2000);
            publishProgress((int) maxprogress);
          }
        } catch (Exception ignored) {}
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
      Intent i = new Intent(MainActivity.this, MahasiswaActivity.class);
      startActivity(i);
      finish();
    }
  }

  public ArrayList<MahasiswaModel> preLoadRaw() {
    ArrayList<MahasiswaModel> mahasiswaModels = new ArrayList<>();
    String line = null;
    BufferedReader reader;
    try {
      Resources res = getResources();
      InputStream raw_dict = res.openRawResource(R.raw.data_mahasiswa);

      reader = new BufferedReader(new InputStreamReader(raw_dict));
      int count = 0;
      do {
        line = reader.readLine();
        String[] splitstr = line.split("\t");

        MahasiswaModel mahasiswaModel;

        mahasiswaModel = new MahasiswaModel(splitstr[0], splitstr[1]);
        mahasiswaModels.add(mahasiswaModel);
        count++;
      } while (line != null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mahasiswaModels;
  }
}
