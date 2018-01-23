package com.ramusthastudio.kamus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import com.ramusthastudio.kamus.model.Kamus;
import com.ramusthastudio.kamus.repository.RepositoryService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PreLoadActivity extends AppCompatActivity {
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pre_load);
    progressBar = findViewById(R.id.progress_bar);
    new LoadDataTask().execute();
  }

  @SuppressLint("StaticFieldLeak")
  private class LoadDataTask extends AsyncTask<Void, Integer, Void> {
    private double progress;
    private double maxprogress = 100;

    @Override
    protected Void doInBackground(Void... params) {
      Boolean firstRun = KamusPrefs.getFirstRun(PreLoadActivity.this);

      if (firstRun) {
        final List<Kamus> englishIndonesiaList = preLoadRaw(R.raw.english_indonesia);
        final List<Kamus> IndonesiaenglishList = preLoadRaw(R.raw.indonesia_english);
        Log.d("PreLoad", "size " + englishIndonesiaList.size());
        Log.d("PreLoad", "size " + IndonesiaenglishList.size());

        progress = 35;
        publishProgress((int) progress);
        Double progressMaxInsert = 80.0;
        Double progressDiff = (progressMaxInsert - progress) / (englishIndonesiaList.size() + englishIndonesiaList.size());

        RepositoryService.beginTransaction();
        try {
          for (Kamus kamus : IndonesiaenglishList) {
            RepositoryService.insertIndonesiaEnglish(kamus);
            progress += progressDiff;
            publishProgress((int) progress);
          }

          for (Kamus kamus : englishIndonesiaList) {
            RepositoryService.insertEnglishIndonesia(kamus);
            progress += progressDiff;
            publishProgress((int) progress);
          }
          // Jika semua proses telah di set success maka akan di commit ke database
          RepositoryService.setTransactionSuccessful();
        } catch (Exception aE) {
          // Jika gagal maka do nothing
          Log.e("PreLoad", "doInBackground: Exception", aE);
        }
        RepositoryService.endTransaction();
        KamusPrefs.setFirstRun(PreLoadActivity.this, false);

        publishProgress((int) maxprogress);
      }
      return null;
    }

    @Override protected void onProgressUpdate(Integer... values) { progressBar.setProgress(values[0]); }

    @Override
    protected void onPostExecute(Void result) {
      Intent i = new Intent(PreLoadActivity.this, MainActivity.class);
      startActivity(i);
      finish();
    }
  }

  private List<Kamus> preLoadRaw(@RawRes int aId) {
    List<Kamus> kamusList = new ArrayList<>();

    try {
      Resources res = getResources();
      InputStream raw_dict = res.openRawResource(aId);

      String line;
      BufferedReader reader = new BufferedReader(new InputStreamReader(raw_dict));
      while (null != (line = reader.readLine())) {
        String[] splitstr = line.split("\t");

        StringBuilder b = new StringBuilder();
        for (int i = 1; i < splitstr.length; i++) {
          b.append(splitstr[i]).append(" ");
        }
        kamusList.add(new Kamus(splitstr[0], b.toString()));
      }
    } catch (Exception aE) {
      Log.e("PreLoad", "Error load raw data ", aE);
    }
    return kamusList;
  }
}
