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
import com.ramusthastudio.cataloguemovie.fragment.DetailMovieFragment;
import com.ramusthastudio.cataloguemovie.fragment.NowPlayingMovieFragment;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.fragment.AbstractMovieFragment.ARG_PARAM;

public class MoviesActivity extends AppCompatActivity {
  public static final String FRAGMENT_INSTANCE = "fragment_state";
  public String fLastTag;
  public Fragment fCurrentFragment;

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

    if (getIntent().getSerializableExtra(ARG_PARAM) != null) {
      final Result result = (Result) getIntent().getSerializableExtra(ARG_PARAM);
      final DetailMovieFragment fr = DetailMovieFragment.newInstance(result);
      fr.setFromOutsideNotif(true);
      replaceFragment(fr, DetailMovieFragment.class.getSimpleName());
    } else {
      if (savedInstanceState != null) {
        final Fragment restored = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_INSTANCE);
        replaceFragment(restored, restored.getClass().getSimpleName());
      } else {
        replaceFragment(NowPlayingMovieFragment.newInstance(), NowPlayingMovieFragment.class.getSimpleName());
      }
    }
  }

  @Override
  protected void onNewIntent(final Intent intent) {
    super.onNewIntent(intent);

    if (intent.getSerializableExtra(ARG_PARAM) != null) {
      final Result result = (Result) intent.getSerializableExtra(ARG_PARAM);
      final FragmentManager fm = getSupportFragmentManager();
      final int count = fm.getBackStackEntryCount();
      final DetailMovieFragment fr = DetailMovieFragment.newInstance(result);

      if (count > 1) {
        replaceFragment(
            fr,
            DetailMovieFragment.class.getSimpleName());
      } else {
        showDetailFragment(
            fr,
            DetailMovieFragment.class.getSimpleName(),
            DetailMovieFragment.class.getSimpleName());
      }
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    final FragmentManager fm = getSupportFragmentManager();
    fm.putFragment(outState, FRAGMENT_INSTANCE, fCurrentFragment);
  }

  @Override
  public void onBackPressed() {
    final FragmentManager fm = getSupportFragmentManager();
    fCurrentFragment = fm.findFragmentByTag(fLastTag);
    super.onBackPressed();
  }

  public final void replaceFragment(Fragment aFragment, String aTag) {
    fLastTag = aTag;
    fCurrentFragment = aFragment;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction
        .replace(R.id.fragment_container, aFragment, aTag)
        .commit();
  }

  public final void showDetailFragment(Fragment aFragment, String aTag, String aName) {
    fCurrentFragment = aFragment;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction
        .addToBackStack(aName)
        .replace(R.id.fragment_container, aFragment, aTag)
        .commit();
  }

  @SuppressLint("ApplySharedPref")
  public final void changeThemePref() {
    final FragmentManager fm = getSupportFragmentManager();
    fCurrentFragment = fm.findFragmentByTag(fLastTag);

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
