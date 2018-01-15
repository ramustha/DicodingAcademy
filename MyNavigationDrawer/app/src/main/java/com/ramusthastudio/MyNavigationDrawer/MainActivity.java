package com.ramusthastudio.MyNavigationDrawer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private CircleImageView profileCircleImageView;
  private final String profileImageUrl = "https://media.licdn.com/mpr/mpr/shrinknp_400_400/AAEAAQAAAAAAAAb8AAAAJGVlMmE5ZmNiLTZlMDQtNDcyMi04OWUzLTcwYWIxZTMzYjhmZA.jpg";
  private DrawerLayout drawer;
  private Toolbar toolbar;
  private ActionBarDrawerToggle toggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("Home");
    }

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Halo ini action dari snackbar", Toast.LENGTH_SHORT).show();
              }
            }).show();
      }
    });
    drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    profileCircleImageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
    Glide.with(MainActivity.this)
        .load(profileImageUrl)
        .into(profileCircleImageView);
    navigationView.setNavigationItemSelectedListener(this);

    if (savedInstanceState == null) {
      Fragment currentFragment = new HomeFragment();
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.content_main, currentFragment)
          .commit();
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    Bundle bundle = new Bundle();

    Fragment fragment = null;

    String title = "";
    if (id == R.id.nav_home) {

      title = "Home";
      fragment = new HomeFragment();

    } else if (id == R.id.nav_camera) {

      title = "Camera";
      fragment = new HalamanFragment();
      bundle.putString(HalamanFragment.EXTRAS, "Camera");
      fragment.setArguments(bundle);

    } else if (id == R.id.nav_gallery) {

      title = "Gallery";
      fragment = new HalamanFragment();
      bundle.putString(HalamanFragment.EXTRAS, "Gallery");
      fragment.setArguments(bundle);

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    /*
    Ganti halaman dengan memanggil fragment replace
     */

    if (fragment != null) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.content_main, fragment)
          .commit();
    }

    getSupportActionBar().setTitle(title);

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
  }

  @Override
  protected void onPause() {
    super.onPause();
    drawer.removeDrawerListener(toggle);
  }
}
