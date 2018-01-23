package com.ramusthastudio.kamus;

import android.os.Bundle;
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
import android.view.Gravity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private DrawerLayout fDrawerLayout;
  private NavigationView fNavigationView;
  private ActionBarDrawerToggle fDrawerToggle;
  private Toolbar fToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fToolbar = findViewById(R.id.toolbar);
    fDrawerLayout = findViewById(R.id.drawer_layout);
    fNavigationView = findViewById(R.id.nav_view);
    fNavigationView.setItemIconTintList(null);
    fNavigationView.setNavigationItemSelectedListener(this);

    setSupportActionBar(fToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("Indonesia-English");
      replaceFragment(IndonesiaEnglishFragment.instance(), "EnglishIndonesiaFragment");
    }

  }

  @Override
  public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.indonesia_english:
        fToolbar.setTitle("Indonesia-English");
        replaceFragment(IndonesiaEnglishFragment.instance(), "EnglishIndonesiaFragment");
        break;
      case R.id.english_indonesia:
        fToolbar.setTitle("English-Indonesia");
        replaceFragment(EnglishIndonesiaFragment.instance(), "EnglishIndonesiaFragment");
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
  }

  @Override
  public void onBackPressed() {
    if (fDrawerLayout.isDrawerOpen(Gravity.START)) {
      fDrawerLayout.closeDrawers();
    } else {
      finish();
    }
  }

  void replaceFragment(Fragment aFragment, String aTag) {
    replaceFragment(aFragment, aTag, null);
  }

  void replaceFragment(Fragment aFragment, String aTag, String aToBackStack) {
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction.replace(R.id.fragment_container, aFragment, aTag);
    mFragmentTransaction.addToBackStack(aToBackStack);
    mFragmentTransaction.commit();
  }
}
