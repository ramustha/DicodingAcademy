package com.ramusthastudio.cataloguemovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.ramusthastudio.cataloguemovie.fragment.DetailMovieFragment;
import com.ramusthastudio.cataloguemovie.fragment.FavoriteMovieFragment;
import com.ramusthastudio.cataloguemovie.fragment.NowPlayingMovieFragment;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.fragment.AbstractMovieFragment.ARG_PARAM;
import static com.ramusthastudio.cataloguemovie.widget.FavoriteWidget.CLICK_ACTION;
import static com.ramusthastudio.cataloguemovie.widget.FavoriteWidget.EXTRA_ID;

public class MoviesActivity extends AppCompatActivity {

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
    setContentView(R.layout.activity_movies);

    if (getIntent().getIntExtra(EXTRA_ID, 0) != 0) {
      final int id = getIntent().getIntExtra(EXTRA_ID, 0);
      replaceFragment(
          FavoriteMovieFragment.newInstance(id),
          FavoriteMovieFragment.class.getSimpleName());
    } else {
      if (getIntent().getSerializableExtra(ARG_PARAM) != null) {
        final Result result = (Result) getIntent().getSerializableExtra(ARG_PARAM);
        replaceFragment(
            NowPlayingMovieFragment.newInstance(result, true),
            NowPlayingMovieFragment.class.getSimpleName());
      } else {
        if (savedInstanceState == null) {
          replaceFragment(NowPlayingMovieFragment.newInstance(),
              NowPlayingMovieFragment.class.getSimpleName());
        }
      }
    }
  }

  @Override
  public void onBackPressed() {
    FragmentManager fm = getSupportFragmentManager();
    if (fm.findFragmentByTag(DetailMovieFragment.class.getSimpleName()) != null) {
      super.onBackPressed();
    } else {
      finish();
    }
  }

  @Override
  protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);

    if (intent.getSerializableExtra(ARG_PARAM) != null) {
      final Result result = (Result) intent.getSerializableExtra(ARG_PARAM);
      showDetailFragment(
          DetailMovieFragment.newInstance(result),
          DetailMovieFragment.class.getSimpleName());
    }
  }

  public final void replaceFragment(Fragment aFragment, String aName) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction transaction = fm.beginTransaction();
    transaction
        .replace(R.id.fragment_container, aFragment, aName)
        .commit();
  }

  public final void showDetailFragment(Fragment aFragment, String aName) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = fm.beginTransaction();
    mFragmentTransaction
        .replace(R.id.fragment_container, aFragment, aName)
        .addToBackStack(null)
        .commit();
  }

  @SuppressLint("ApplySharedPref")
  public final void changeThemePref() {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final SharedPreferences.Editor edit = sp.edit();
    boolean useThemeLight = sp.getBoolean("useThemeLight", false);

    if (!useThemeLight) {
      edit.putBoolean("useThemeLight", true);
      Log.d(MoviesActivity.class.getSimpleName(), "Light");
    } else {
      edit.putBoolean("useThemeLight", false);
      Log.d(MoviesActivity.class.getSimpleName(), "Dark");
    }
    edit.commit();
    recreate();
  }
}
