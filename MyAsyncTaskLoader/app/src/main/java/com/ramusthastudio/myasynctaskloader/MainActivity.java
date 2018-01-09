package com.ramusthastudio.myasynctaskloader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.ramusthastudio.myasynctaskloader.model.Result;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Result> {
  private Button fSearchTv;
  private ListView fListView;
  private EditText fCityChangeEdt;
  private WeatherAdapter fWeatherAdapter;

  static final String EXTRAS_CITY = "EXTRAS_CITY";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fListView = findViewById(R.id.listView);
    fCityChangeEdt = findViewById(R.id.edit_kota);
    fSearchTv = findViewById(R.id.btn_kota);

    fWeatherAdapter = new WeatherAdapter(this);
    fWeatherAdapter.notifyDataSetChanged();
    fListView.setAdapter(fWeatherAdapter);

    fSearchTv.setOnClickListener(myListener);

    String city = fCityChangeEdt.getText().toString();
    Bundle bundle = new Bundle();
    bundle.putString(EXTRAS_CITY, city);

    getSupportLoaderManager().initLoader(0, bundle, MainActivity.this);
  }

  @Override public void onLoaderReset(Loader<Result> loader) { fWeatherAdapter.setOpenWeathersData(null); }
  @Override public Loader<Result> onCreateLoader(int id, Bundle args) { return new MyAsyncTaskLoader(this); }
  @Override public void onLoadFinished(Loader<Result> loader, Result data) { fWeatherAdapter.setOpenWeathersData(data); }

  View.OnClickListener myListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      String kota = fCityChangeEdt.getText().toString();

      if (TextUtils.isEmpty(kota)) return;

      Bundle bundle = new Bundle();
      bundle.putString(EXTRAS_CITY, kota);
      getSupportLoaderManager().restartLoader(0, bundle, MainActivity.this);
    }
  };
}
