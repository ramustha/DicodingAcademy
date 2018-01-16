package com.ramusthastudio.cataloguemovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.ramusthastudio.cataloguemovie.model.Result;

import static com.ramusthastudio.cataloguemovie.MainFragment.ARG_PARAM;

public class MainActivity extends AppCompatActivity {
  public static final int REQUEST_CODE = 111;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (getIntent().getSerializableExtra(ARG_PARAM) != null) {
      final Result result = (Result) getIntent().getSerializableExtra(ARG_PARAM);
      addFragment(MainFragment.newInstance(result), MainFragment.class.getSimpleName());
    } else {
      addFragment(MainFragment.newInstance(), MainFragment.class.getSimpleName());
    }

  }

  void addFragment(Fragment aFragment, String aTag) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.fragment_container, aFragment, aTag);
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
    mFragmentTransaction.replace(R.id.fragment_container, aFragment, aTag);
    mFragmentTransaction.addToBackStack(aToBackStack);
    mFragmentTransaction.commit();
  }
}
