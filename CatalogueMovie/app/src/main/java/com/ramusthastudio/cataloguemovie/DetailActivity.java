package com.ramusthastudio.cataloguemovie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.DetailFragment.ARG_PARAM;

public class DetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    boolean useThemeLight = sp.getBoolean("useThemeLight", false);

    if (useThemeLight) {
      getTheme().applyStyle(R.style.AppThemeLight, true);
    } else {
      getTheme().applyStyle(R.style.AppThemeDark, true);
    }

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    if (getIntent().getSerializableExtra(ARG_PARAM) != null) {
      final Result result = (Result) getIntent().getSerializableExtra(ARG_PARAM);
      replaceFragment(DetailFragment.newInstance(result), DetailFragment.class.getSimpleName());
    }
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  void replaceFragment(Fragment aFragment, String aTag) {
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction
        .replace(R.id.fragment_container, aFragment, aTag)
        .commit();
  }
}