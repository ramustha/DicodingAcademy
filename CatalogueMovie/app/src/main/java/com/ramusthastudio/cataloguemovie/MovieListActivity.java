package com.ramusthastudio.cataloguemovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.AbstractMovieFragment.ARG_PARAM;

public class MovieListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  public static final int REQUEST_CODE = 111;
  private DrawerLayout fDrawerLayout;
  private NavigationView fNavigationView;
  private ActionBarDrawerToggle fDrawerToggle;
  private Toolbar fToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    boolean useThemeLight = sp.getBoolean("useThemeLight", false);

    if (!useThemeLight) {
      getTheme().applyStyle(R.style.AppThemeLight, true);
    } else {
      getTheme().applyStyle(R.style.AppThemeDark, true);
    }

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_list);
    fToolbar = findViewById(R.id.toolbar);
    fDrawerLayout = findViewById(R.id.drawer_layout);
    fNavigationView = findViewById(R.id.nav_view);

    setSupportActionBar(fToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.menu_now_playing));
    }

    fNavigationView.setItemIconTintList(null);
    fNavigationView.setNavigationItemSelectedListener(this);

    if (getIntent().getSerializableExtra(ARG_PARAM) != null) {
      final Result result = (Result) getIntent().getSerializableExtra(ARG_PARAM);
      replaceFragment(NowPlayingMovieFragment.newInstance(result), NowPlayingMovieFragment.class.getSimpleName());
    } else {
      replaceFragment(NowPlayingMovieFragment.newInstance(), NowPlayingMovieFragment.class.getSimpleName());
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.now_playing:
        fToolbar.setTitle(getString(R.string.menu_now_playing));
        replaceFragment(NowPlayingMovieFragment.newInstance(), NowPlayingMovieFragment.class.getSimpleName());
        break;
      case R.id.upcoming:
        fToolbar.setTitle(getString(R.string.menu_upcoming));
        replaceFragment(UpcomingMovieFragment.newInstance(), UpcomingMovieFragment.class.getSimpleName());
        break;
      case R.id.popular:
        fToolbar.setTitle(getString(R.string.menu_popular));
        replaceFragment(PopularMovieFragment.newInstance(), PopularMovieFragment.class.getSimpleName());
        break;
      case R.id.top_rated:
        fToolbar.setTitle(getString(R.string.menu_top_rated));
        replaceFragment(TopRatedMovieFragment.newInstance(), TopRatedMovieFragment.class.getSimpleName());
        break;
      case R.id.favorite:
        fToolbar.setTitle(getString(R.string.menu_favorite));
        replaceFragment(FavoriteFragment.newInstance(), FavoriteFragment.class.getSimpleName());
        break;
      case R.id.change_theme:
        changeThemePref();
        break;
      case R.id.change_language:
        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        break;
    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    fDrawerToggle = new ActionBarDrawerToggle(
        this, fDrawerLayout, fToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    fDrawerLayout.addDrawerListener(fDrawerToggle);
    fDrawerToggle.syncState();
  }

  @Override
  protected void onPause() {
    super.onPause();
    fDrawerLayout.removeDrawerListener(fDrawerToggle);
    removeFragment(NowPlayingMovieFragment.newInstance());
  }

  @Override
  public void onBackPressed() {
    if (fDrawerLayout.isDrawerOpen(Gravity.START)) {
      fDrawerLayout.closeDrawers();
    } else {

      if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
        finish();
      } else {
        super.onBackPressed();
      }
    }
  }

  void removeFragment(Fragment aFragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.remove(aFragment)
        .commit();
  }

  void replaceFragment(Fragment aFragment, String aTag) {
    replaceFragment(aFragment, aTag, null);
  }

  void replaceFragment(Fragment aFragment, String aTag, String aName) {
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction
        .addToBackStack(aName)
        .replace(R.id.fragment_container, aFragment, aTag)
        .commit();
  }

  void changeThemePref() {
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
