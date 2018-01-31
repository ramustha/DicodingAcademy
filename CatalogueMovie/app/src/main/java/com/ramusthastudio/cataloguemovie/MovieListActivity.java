package com.ramusthastudio.cataloguemovie;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.ramusthastudio.cataloguemovie.fragment.NowPlayingMovieFragment;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.fragment.AbstractMovieFragment.ARG_PARAM;

public class MovieListActivity extends AppCompatActivity {
  public static final String FRAGMENT_KEY = "fragment_key";
  public static final int REQUEST_CODE = 111;
  private Fragment currentFragment;

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
    setContentView(R.layout.activity_movie_list);

    if (getIntent().getSerializableExtra(ARG_PARAM) != null) {
      Log.d("Main", "notif fragment");
      final Result result = (Result) getIntent().getSerializableExtra(ARG_PARAM);
      currentFragment = NowPlayingMovieFragment.newInstance(result);
      replaceFragment(currentFragment, NowPlayingMovieFragment.class.getSimpleName());
    } else {
      if (savedInstanceState != null) {
        Log.d("Main", "restore fragment");
        currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
        replaceFragment(currentFragment, NowPlayingMovieFragment.class.getSimpleName());
      } else {
        Log.d("Main", "init fragment");
        currentFragment = NowPlayingMovieFragment.newInstance();
        replaceFragment(currentFragment, NowPlayingMovieFragment.class.getSimpleName());
      }
    }
  }

  @Override
  protected void onSaveInstanceState(final Bundle outState) {
    getSupportFragmentManager().putFragment(outState, FRAGMENT_KEY, currentFragment);
    super.onSaveInstanceState(outState);
    Log.d("Main", "saved fragment");
  }

  @Override
  public void onBackPressed() {
    final int count = getSupportFragmentManager().getBackStackEntryCount();
    if (count > 1) {
      super.onBackPressed();
    } else {
      finish();
    }
  }

  public final void replaceFragment(Fragment aFragment, String aTag) {
    replaceFragment(aFragment, aTag, null);
  }

  public final void replaceFragment(Fragment aFragment, String aTag, String aName) {
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction
        .addToBackStack(aName)
        .replace(R.id.fragment_container, aFragment, aTag)
        .commit();
  }

  @SuppressLint("ApplySharedPref")
  public final void changeThemePref() {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    final SharedPreferences.Editor edit = sp.edit();
    boolean useThemeLight = sp.getBoolean("useThemeLight", false);

    if (!useThemeLight) {
      edit.putBoolean("useThemeLight", true);
      Log.d(MovieListActivity.class.getSimpleName(), "Light");
    } else {
      edit.putBoolean("useThemeLight", false);
      Log.d(MovieListActivity.class.getSimpleName(), "Dark");
    }
    edit.commit();
    recreate();
  }
}
