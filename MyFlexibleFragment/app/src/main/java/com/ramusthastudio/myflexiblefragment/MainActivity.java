package com.ramusthastudio.myflexiblefragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    addFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
  }

  void addFragment(Fragment aFragment, String aTag) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.frame_container, aFragment, aTag);
    Log.d("MyFlexibleFragment", "Fragment Name :" + aTag);
    fragmentTransaction.commit();
  }

  void removeFragment(Fragment aFragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.remove(aFragment);
    fragmentTransaction.commit();
  }

  void replaceFragment(Fragment aFragment, String aTag) {
    replaceFragment(aFragment, aTag, null);
  }

  void replaceFragment(Fragment aFragment, String aTag, String aToBackStack) {
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction.replace(R.id.frame_container, aFragment, aTag);
    mFragmentTransaction.addToBackStack(aToBackStack);
    Log.d("MyFlexibleFragment", "Fragment Name :" + aTag);
    mFragmentTransaction.commit();
  }
}
